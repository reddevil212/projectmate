package com.proj.mate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.mate.dto.AiProjectAnalysisResponse;
import com.proj.mate.dto.ProjectResponseDto;
import com.proj.mate.entity.*;
import com.proj.mate.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-2.5-flash}")
    private String modelName;

    private final ObjectMapper objectMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final SkillRepository skillRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final ProjectRoleSkillRepository projectRoleSkillRepository;
    private final ModelMapper modelMapper;

    public AiService(ObjectMapper objectMapper,
                     ProjectRepository projectRepository,
                     UserRepository userRepository,
                     ProjectMemberRepository projectMemberRepository,
                     SkillRepository skillRepository,
                     ProjectSkillRepository projectSkillRepository,
                     ProjectRoleRepository projectRoleRepository,
                     ProjectRoleSkillRepository projectRoleSkillRepository,
                     ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.skillRepository = skillRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.projectRoleSkillRepository = projectRoleSkillRepository;
        this.modelMapper = modelMapper;
    }

    public AiProjectAnalysisResponse analyzeProject(String userPrompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return generateFallbackAnalysis(userPrompt);
        }

        try {
            String systemInstruction = """
                You are an expert software architect and technical project analyst.
                The user will provide a project idea or requirements prompt.
                Analyze the user's idea and generate a structured JSON object matching the following fields:

                1. "name": A concise, creative, and professional project name based on the user's idea.
                2. "type": The project domain/category (e.g. "Web Application", "Full Stack", "Mobile App", "AI/ML").
                3. "description": A comprehensive, multi-paragraph summary describing the project scope, architecture, core features, and target goals.
                4. "memberCount": MUST BE EXACTLY 4.
                5. "status": "OPEN".
                6. "projectSkills": An array of overall tech stack required for the project. Each item MUST have:
                   - "skillName": Name of the technology/skill (e.g. "Next.js", "Python", "FastAPI", "PostgreSQL", "Docker").
                   - "level": Required experience level, MUST be one of ["Beginner", "Intermediate", "Expert"].
                7. "roles": MUST CONTAIN EXACTLY 4 ROLES for a 4-member team (e.g. "Frontend Engineer", "Backend Engineer", "DevOps Specialist", "UI/UX Designer").
                   Each role item MUST have:
                   - "roleName": Name/Title of the role.
                   - "skills": An array of required skills for this role, where each item has:
                     - "skillName": Name of the skill.
                     - "proficiencyRequired": An integer score from 1 (basic) to 5 (expert).

                User Prompt: %s

                Return ONLY valid raw JSON without extra formatting text or code blocks.
                """.formatted(userPrompt);

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", systemInstruction)
                            ))
                    ),
                    "generationConfig", Map.of(
                            "responseMimeType", "application/json"
                    )
            );

            RestClient restClient = RestClient.create();
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key=" + apiKey;

            String responseJson = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode rootNode = objectMapper.readTree(responseJson);
            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && !candidatesNode.isEmpty()) {
                String textContent = candidatesNode.get(0).path("content").path("parts").get(0).path("text").asText();

                if (textContent.startsWith("```json")) {
                    textContent = textContent.substring(7);
                }
                if (textContent.startsWith("```")) {
                    textContent = textContent.substring(3);
                }
                if (textContent.endsWith("```")) {
                    textContent = textContent.substring(0, textContent.length() - 3);
                }
                textContent = textContent.trim();

                AiProjectAnalysisResponse response = objectMapper.readValue(textContent, AiProjectAnalysisResponse.class);
                response.setMemberCount(4); // Enforce 4 member team count
                response.setStatus("OPEN");
                return response;
            }
        } catch (Exception e) {
            // Log and fallback gracefully
            System.err.println("Error calling Gemini API: " + e.getMessage());
        }

        return generateFallbackAnalysis(userPrompt);
    }

    @Transactional
    public ProjectResponseDto createProjectFromAiAnalysis(AiProjectAnalysisResponse analysis, Long ownerId) {
        UserInfo owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + ownerId));

        // 1. Create Project
        Project project = Project.builder()
                .owner(owner)
                .name(analysis.getName())
                .type(analysis.getType())
                .description(analysis.getDescription())
                .memberCount(4)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .build();

        Project savedProject = projectRepository.save(project);

        // 2. Add owner as OWNER member
        ProjectMember ownerMember = ProjectMember.builder()
                .project(savedProject)
                .user(owner)
                .role("OWNER")
                .assignedAt(LocalDateTime.now())
                .build();
        projectMemberRepository.save(ownerMember);

        // 3. Process Project Skills
        if (analysis.getProjectSkills() != null) {
            for (AiProjectAnalysisResponse.ProjectSkillRequirementDto skillDto : analysis.getProjectSkills()) {
                Skill skill = getOrCreateSkill(skillDto.getSkillName());
                ProjectSkill projectSkill = ProjectSkill.builder()
                        .project(savedProject)
                        .skill(skill)
                        .level(skillDto.getLevel() != null ? skillDto.getLevel() : "Intermediate")
                        .build();
                projectSkillRepository.save(projectSkill);
            }
        }

        // 4. Process Roles and Role Skills
        if (analysis.getRoles() != null) {
            for (AiProjectAnalysisResponse.RoleRequirementDto roleDto : analysis.getRoles()) {
                ProjectRole role = getOrCreateRole(roleDto.getRoleName());

                if (roleDto.getSkills() != null) {
                    for (AiProjectAnalysisResponse.RoleSkillRequirementDto roleSkillDto : roleDto.getSkills()) {
                        Skill skill = getOrCreateSkill(roleSkillDto.getSkillName());
                        ProjectRoleSkill roleSkill = ProjectRoleSkill.builder()
                                .projectRole(role)
                                .skill(skill)
                                .proficiencyRequired(roleSkillDto.getProficiencyRequired() > 0 ? roleSkillDto.getProficiencyRequired() : 3)
                                .build();
                        projectRoleSkillRepository.save(roleSkill);
                    }
                }
            }
        }

        return modelMapper.map(savedProject, ProjectResponseDto.class);
    }

    private Skill getOrCreateSkill(String skillName) {
        if (skillName == null || skillName.trim().isEmpty()) {
            skillName = "General Skill";
        }
        String finalName = skillName.trim();
        return skillRepository.findByNameIgnoreCase(finalName)
                .orElseGet(() -> skillRepository.save(Skill.builder().name(finalName).build()));
    }

    private ProjectRole getOrCreateRole(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            roleName = "Software Engineer";
        }
        String finalName = roleName.trim();
        return projectRoleRepository.findByNameIgnoreCase(finalName)
                .orElseGet(() -> projectRoleRepository.save(ProjectRole.builder().name(finalName).build()));
    }

    private AiProjectAnalysisResponse generateFallbackAnalysis(String userPrompt) {
        List<AiProjectAnalysisResponse.ProjectSkillRequirementDto> projectSkills = List.of(
                new AiProjectAnalysisResponse.ProjectSkillRequirementDto("Next.js", "Expert"),
                new AiProjectAnalysisResponse.ProjectSkillRequirementDto("Python", "Intermediate"),
                new AiProjectAnalysisResponse.ProjectSkillRequirementDto("FastAPI", "Intermediate"),
                new AiProjectAnalysisResponse.ProjectSkillRequirementDto("PostgreSQL", "Intermediate")
        );

        List<AiProjectAnalysisResponse.RoleRequirementDto> roles = List.of(
                new AiProjectAnalysisResponse.RoleRequirementDto("Frontend Lead", List.of(
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("Next.js", 5),
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("TypeScript", 4)
                )),
                new AiProjectAnalysisResponse.RoleRequirementDto("Backend Engineer", List.of(
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("Python", 5),
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("FastAPI", 4),
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("PostgreSQL", 3)
                )),
                new AiProjectAnalysisResponse.RoleRequirementDto("DevOps Engineer", List.of(
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("Docker", 4),
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("CI/CD", 3)
                )),
                new AiProjectAnalysisResponse.RoleRequirementDto("UI/UX Designer", List.of(
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("Figma", 4),
                        new AiProjectAnalysisResponse.RoleSkillRequirementDto("Prototyping", 3)
                ))
        );

        return AiProjectAnalysisResponse.builder()
                .name("Next.js & Python Web Platform")
                .type("Full Stack")
                .description("Project generated from prompt: " + userPrompt + ". A modern web application combining Next.js frontend with Python backend service.")
                .memberCount(4)
                .status("OPEN")
                .projectSkills(projectSkills)
                .roles(roles)
                .build();
    }
}
