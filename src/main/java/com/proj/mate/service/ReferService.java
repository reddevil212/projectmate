package com.proj.mate.service;

import com.proj.mate.dto.ReferRequestDto;
import com.proj.mate.dto.TeamSuggestionResponseDto;
import com.proj.mate.dto.UserResponseDto;
import com.proj.mate.entity.*;
import com.proj.mate.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReferService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final ProjectRoleSkillRepository projectRoleSkillRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;

    public ReferService(UserRepository userRepository,
                        UserSkillRepository userSkillRepository,
                        ProjectRepository projectRepository,
                        ProjectRoleRepository projectRoleRepository,
                        ProjectRoleSkillRepository projectRoleSkillRepository,
                        ProjectSkillRepository projectSkillRepository,
                        ProjectMemberRepository projectMemberRepository,
                        ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userSkillRepository = userSkillRepository;
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.projectRoleSkillRepository = projectRoleSkillRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Suggests candidate team members for an existing project by evaluating
     * all candidate users against the project's roles and tech stack requirements.
     */
    @Transactional(readOnly = true)
    public TeamSuggestionResponseDto suggestTeamForProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        // 1. Get existing member user IDs to exclude them
        Set<Long> existingMemberUserIds = projectMemberRepository.findByProjectId(projectId)
                .stream()
                .map(member -> member.getUser().getId())
                .collect(Collectors.toSet());

        // 2. Fetch project roles and their role skills
        List<ProjectRole> allRoles = projectRoleRepository.findAll();
        List<TeamSuggestionResponseDto.RoleMatchRecommendationDto> roleRecommendations = new ArrayList<>();

        for (ProjectRole role : allRoles) {
            List<ProjectRoleSkill> roleSkills = projectRoleSkillRepository.findByProjectRoleId(role.getId());
            if (roleSkills.isEmpty()) {
                continue;
            }

            List<ReferRequestDto.CustomSkillRequirement> skillRequirements = roleSkills.stream()
                    .map(prs -> new ReferRequestDto.CustomSkillRequirement(
                            prs.getSkill().getName(),
                            prs.getProficiencyRequired()
                    ))
                    .collect(Collectors.toList());

            List<TeamSuggestionResponseDto.UserCandidateMatchDto> topCandidates = evaluateCandidatesForSkills(
                    skillRequirements,
                    existingMemberUserIds,
                    5 // Top 5 candidates per role
            );

            if (!topCandidates.isEmpty()) {
                roleRecommendations.add(TeamSuggestionResponseDto.RoleMatchRecommendationDto.builder()
                        .roleId(role.getId())
                        .roleName(role.getName())
                        .topCandidates(topCandidates)
                        .build());
            }
        }

        return TeamSuggestionResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .totalRolesRequired(roleRecommendations.size())
                .roleRecommendations(roleRecommendations)
                .build();
    }

    /**
     * Recommends team candidates based on AI-analyzed roles and required skills.
     */
    @Transactional(readOnly = true)
    public TeamSuggestionResponseDto suggestTeamFromAiAnalysis(ReferRequestDto requestDto) {
        String projectName = "AI Suggested Project";
        Long projectId = requestDto.getProjectId();

        Set<Long> excludedUserIds = new HashSet<>();
        if (projectId != null) {
            projectRepository.findById(projectId).ifPresent(p -> {
                excludedUserIds.addAll(
                        projectMemberRepository.findByProjectId(projectId)
                                .stream()
                                .map(m -> m.getUser().getId())
                                .collect(Collectors.toSet())
                );
            });
        }

        List<TeamSuggestionResponseDto.RoleMatchRecommendationDto> roleRecommendations = new ArrayList<>();

        if (requestDto.getCustomRoles() != null) {
            for (ReferRequestDto.CustomRoleRequirement customRole : requestDto.getCustomRoles()) {
                List<ReferRequestDto.CustomSkillRequirement> requiredSkills =
                        customRole.getRequiredSkills() != null ? customRole.getRequiredSkills() : Collections.emptyList();

                List<TeamSuggestionResponseDto.UserCandidateMatchDto> candidates =
                        evaluateCandidatesForSkills(requiredSkills, excludedUserIds, 5);

                roleRecommendations.add(TeamSuggestionResponseDto.RoleMatchRecommendationDto.builder()
                        .roleName(customRole.getRoleName())
                        .topCandidates(candidates)
                        .build());
            }
        }

        return TeamSuggestionResponseDto.builder()
                .projectId(projectId)
                .projectName(projectName)
                .totalRolesRequired(roleRecommendations.size())
                .roleRecommendations(roleRecommendations)
                .build();
    }

    /**
     * Ranks all candidate users for a specific project role.
     */
    @Transactional(readOnly = true)
    public TeamSuggestionResponseDto.RoleMatchRecommendationDto matchUsersForRole(Long roleId, Long projectId) {
        ProjectRole role = projectRoleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Project role not found with id: " + roleId));

        Set<Long> excludedUserIds = new HashSet<>();
        if (projectId != null) {
            excludedUserIds.addAll(
                    projectMemberRepository.findByProjectId(projectId)
                            .stream()
                            .map(m -> m.getUser().getId())
                            .collect(Collectors.toSet())
            );
        }

        List<ProjectRoleSkill> roleSkills = projectRoleSkillRepository.findByProjectRoleId(roleId);
        List<ReferRequestDto.CustomSkillRequirement> skillRequirements = roleSkills.stream()
                .map(prs -> new ReferRequestDto.CustomSkillRequirement(
                        prs.getSkill().getName(),
                        prs.getProficiencyRequired()
                ))
                .collect(Collectors.toList());

        List<TeamSuggestionResponseDto.UserCandidateMatchDto> candidates =
                evaluateCandidatesForSkills(skillRequirements, excludedUserIds, 10);

        return TeamSuggestionResponseDto.RoleMatchRecommendationDto.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .topCandidates(candidates)
                .build();
    }

    /**
     * Evaluates all non-excluded candidate users against a list of required skills
     * and returns top candidates sorted by match score.
     */
    private List<TeamSuggestionResponseDto.UserCandidateMatchDto> evaluateCandidatesForSkills(
            List<ReferRequestDto.CustomSkillRequirement> requiredSkills,
            Set<Long> excludedUserIds,
            int limit) {

        if (requiredSkills.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserInfo> allUsers = userRepository.findAll();
        List<TeamSuggestionResponseDto.UserCandidateMatchDto> candidates = new ArrayList<>();

        for (UserInfo user : allUsers) {
            if (excludedUserIds.contains(user.getId())) {
                continue; // Skip existing team members
            }

            List<UserSkill> userSkills = userSkillRepository.findByUserId(user.getId());
            Map<String, Integer> userSkillMap = new HashMap<>();
            for (UserSkill us : userSkills) {
                userSkillMap.put(us.getSkill().getName().toLowerCase().trim(), us.getProficiency());
            }

            double totalSkillScoreSum = 0.0;
            int matchedSkillsCount = 0;

            List<TeamSuggestionResponseDto.SkillMatchDetailDto> matchedSkills = new ArrayList<>();
            List<TeamSuggestionResponseDto.SkillMatchDetailDto> missingSkills = new ArrayList<>();

            for (ReferRequestDto.CustomSkillRequirement reqSkill : requiredSkills) {
                String reqSkillNameKey = reqSkill.getSkillName().toLowerCase().trim();
                int requiredProf = reqSkill.getProficiencyRequired() > 0 ? reqSkill.getProficiencyRequired() : 1;

                if (userSkillMap.containsKey(reqSkillNameKey)) {
                    int userProf = userSkillMap.get(reqSkillNameKey);
                    matchedSkillsCount++;

                    // Calculate percentage match for this skill
                    double skillMatch = Math.min(100.0, ((double) userProf / requiredProf) * 100.0);
                    totalSkillScoreSum += skillMatch;

                    matchedSkills.add(TeamSuggestionResponseDto.SkillMatchDetailDto.builder()
                            .skillName(reqSkill.getSkillName())
                            .userProficiency(userProf)
                            .requiredProficiency(requiredProf)
                            .isMatched(true)
                            .build());
                } else {
                    missingSkills.add(TeamSuggestionResponseDto.SkillMatchDetailDto.builder()
                            .skillName(reqSkill.getSkillName())
                            .userProficiency(0)
                            .requiredProficiency(requiredProf)
                            .isMatched(false)
                            .build());
                }
            }

            // Overall Match Score (%) = Average of individual skill scores
            double overallMatchScore = Math.round((totalSkillScoreSum / requiredSkills.size()) * 10.0) / 10.0;

            // Only consider candidates with at least 1 matching skill or > 0 score
            if (matchedSkillsCount > 0 || overallMatchScore > 0) {
                UserResponseDto userDto = modelMapper.map(user, UserResponseDto.class);

                candidates.add(TeamSuggestionResponseDto.UserCandidateMatchDto.builder()
                        .candidate(userDto)
                        .matchScore(overallMatchScore)
                        .totalRequiredSkills(requiredSkills.size())
                        .matchedSkillsCount(matchedSkillsCount)
                        .matchedSkills(matchedSkills)
                        .missingSkills(missingSkills)
                        .build());
            }
        }

        // Sort candidates by match score descending
        candidates.sort((c1, c2) -> Double.compare(c2.getMatchScore(), c1.getMatchScore()));

        return candidates.stream().limit(limit).collect(Collectors.toList());
    }
}
