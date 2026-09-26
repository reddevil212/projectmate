package com.proj.mate.service;

import com.proj.mate.dto.ProjectSkillRequestDto;
import com.proj.mate.dto.ProjectSkillResponseDto;
import com.proj.mate.entity.Project;
import com.proj.mate.entity.ProjectSkill;
import com.proj.mate.entity.Skill;
import com.proj.mate.repository.ProjectRepository;
import com.proj.mate.repository.ProjectSkillRepository;
import com.proj.mate.repository.SkillRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectSkillService {

    private final ProjectSkillRepository projectSkillRepository;
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;

    public ProjectSkillService(ProjectSkillRepository projectSkillRepository,
                               ProjectRepository projectRepository,
                               SkillRepository skillRepository) {
        this.projectSkillRepository = projectSkillRepository;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
    }

    private ProjectSkillResponseDto convertToDto(ProjectSkill projectSkill) {
        return ProjectSkillResponseDto.builder()
                .id(projectSkill.getId())
                .projectId(projectSkill.getProject().getId())
                .skill(projectSkill.getSkill())
                .level(projectSkill.getLevel())
                .build();
    }

    @Transactional
    public ProjectSkillResponseDto addSkillToProject(ProjectSkillRequestDto requestDto) {
        Project project = projectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + requestDto.getProjectId()));

        Skill skill = skillRepository.findById(requestDto.getSkillId())
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + requestDto.getSkillId()));

        Optional<ProjectSkill> existing = projectSkillRepository.findByProjectIdAndSkillId(requestDto.getProjectId(), requestDto.getSkillId());
        if (existing.isPresent()) {
            ProjectSkill projectSkill = existing.get();
            projectSkill.setLevel(requestDto.getLevel());
            return convertToDto(projectSkillRepository.save(projectSkill));
        }

        ProjectSkill projectSkill = ProjectSkill.builder()
                .project(project)
                .skill(skill)
                .level(requestDto.getLevel())
                .build();

        return convertToDto(projectSkillRepository.save(projectSkill));
    }

    @Transactional(readOnly = true)
    public Optional<ProjectSkillResponseDto> getProjectSkillById(Long id) {
        return projectSkillRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<ProjectSkillResponseDto> getSkillsByProjectId(Long projectId) {
        return projectSkillRepository.findByProjectId(projectId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectSkillResponseDto> getProjectsBySkillId(Long skillId) {
        return projectSkillRepository.findBySkillId(skillId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectSkillResponseDto> getSkillsByProjectIdAndLevel(Long projectId, String level) {
        return projectSkillRepository.findByProjectIdAndLevel(projectId, level)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<ProjectSkillResponseDto> updateProjectSkillLevel(Long id, String level) {
        return projectSkillRepository.findById(id)
                .map(ps -> {
                    ps.setLevel(level);
                    return convertToDto(projectSkillRepository.save(ps));
                });
    }

    @Transactional
    public boolean deleteProjectSkill(Long id) {
        if (!projectSkillRepository.existsById(id)) {
            return false;
        }
        projectSkillRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean deleteSkillFromProject(Long projectId, Long skillId) {
        Optional<ProjectSkill> ps = projectSkillRepository.findByProjectIdAndSkillId(projectId, skillId);
        if (ps.isPresent()) {
            projectSkillRepository.delete(ps.get());
            return true;
        }
        return false;
    }
}
