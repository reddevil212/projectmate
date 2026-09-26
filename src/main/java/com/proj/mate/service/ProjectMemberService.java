package com.proj.mate.service;

import com.proj.mate.dto.ProjectMemberResponseDto;
import com.proj.mate.dto.UserResponseDto;
import com.proj.mate.entity.Project;
import com.proj.mate.entity.ProjectMember;
import com.proj.mate.entity.UserInfo;
import com.proj.mate.repository.ProjectMemberRepository;
import com.proj.mate.repository.ProjectRepository;
import com.proj.mate.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectMemberService {

    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public ProjectMemberService(UserRepository userRepository,
                                ProjectMemberRepository projectMemberRepository,
                                ProjectRepository projectRepository,
                                ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    private ProjectMemberResponseDto convertToDto(ProjectMember member) {
        return ProjectMemberResponseDto.builder()
                .id(member.getId())
                .projectId(member.getProject().getId())
                .user(modelMapper.map(member.getUser(), UserResponseDto.class))
                .role(member.getRole())
                .roleInProject(member.getRoleInProject())
                .assignedAt(member.getAssignedAt())
                .build();
    }

    @Transactional
    public ProjectMemberResponseDto addMemberToProject(Long projectId, Long userId, String role, String roleInProject) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (projectMemberRepository.findByProjectAndUser(project, user).isPresent()) {
            throw new IllegalArgumentException("User is already a member of this project");
        }

        String assignedRole = (role != null && !role.trim().isEmpty()) ? role : "MEMBER";

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(assignedRole)
                .roleInProject(roleInProject) // initiates with null if not provided
                .assignedAt(LocalDateTime.now())
                .build();

        ProjectMember savedMember = projectMemberRepository.save(projectMember);
        return convertToDto(savedMember);
    }

    @Transactional
    public ProjectMemberResponseDto addMemberToProject(Long projectId, Long userId, String role) {
        return addMemberToProject(projectId, userId, role, null);
    }

    @Transactional
    public ProjectMemberResponseDto addMemberToProject(Long projectId, Long userId) {
        return addMemberToProject(projectId, userId, "MEMBER", null);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new IllegalArgumentException("Project member not found"));
        projectMemberRepository.delete(projectMember);
    }

    @Transactional(readOnly = true)
    public boolean isMemberOfProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return projectMemberRepository.findByProjectAndUser(project, user).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isProjectOwner(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return project.getOwner().getId().equals(user.getId());
    }

    @Transactional
    public ProjectMemberResponseDto updateMemberRole(Long projectId, Long userId, String newRole) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new IllegalArgumentException("Project member not found"));
        projectMember.setRole(newRole);
        ProjectMember updatedMember = projectMemberRepository.save(projectMember);
        return convertToDto(updatedMember);
    }

    @Transactional
    public ProjectMemberResponseDto updateRoleInProject(Long projectId, Long userId, String roleInProject) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new IllegalArgumentException("Project member not found"));
        projectMember.setRoleInProject(roleInProject);
        ProjectMember updatedMember = projectMemberRepository.save(projectMember);
        return convertToDto(updatedMember);
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDto> getMembersByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        return projectMemberRepository.findByProject(project)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDto> getProjectsByUserId(Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return projectMemberRepository.findByUser(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
