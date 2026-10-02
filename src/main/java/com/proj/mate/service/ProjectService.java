package com.proj.mate.service;

import com.proj.mate.dto.ProjectRequestDto;
import com.proj.mate.dto.ProjectResponseDto;
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
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          ProjectMemberRepository projectMemberRepository,
                          ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.modelMapper = modelMapper;
    }

    private ProjectResponseDto convertToDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }

    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto requestDto) {

        UserInfo owner = userRepository.findById(requestDto.getOwnerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: " + requestDto.getOwnerId()
                        )
                );

        Project project = Project.builder()
                .owner(owner)
                .name(requestDto.getName())
                .type(requestDto.getType())
                .description(requestDto.getDescription())
                .memberCount(requestDto.getMemberCount())
                .visibility(
                        requestDto.getVisibility() != null && !requestDto.getVisibility().trim().isEmpty()
                                ? requestDto.getVisibility()
                                : "PUBLIC"
                )
                .latestUpdate(requestDto.getLatestUpdate())
                .createdAt(
                        requestDto.getCreatedAt() != null
                                ? requestDto.getCreatedAt()
                                : LocalDateTime.now()
                )
                .status(
                        requestDto.getStatus() != null
                                ? requestDto.getStatus()
                                : "OPEN"
                )
                .build();

        Project savedProject = projectRepository.save(project);

        // Automatically assign creator/owner as a project member with OWNER role
        ProjectMember ownerMember = ProjectMember.builder()
                .project(savedProject)
                .user(owner)
                .role("OWNER")
                .assignedAt(LocalDateTime.now())
                .build();
        projectMemberRepository.save(ownerMember);

        return convertToDto(savedProject);
    }

    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Project not found with id: " + id
                        )
                );
        return convertToDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectByOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectByStatus(String status) {
        return projectRepository.findByStatus(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectByType(String type) {
        return projectRepository.findByType(type)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectByVisibility(String visibility) {
        return projectRepository.findByVisibility(visibility)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectByOwnerIdAndStatus(
            Long ownerId,
            String status
    ) {
        return projectRepository.findByOwnerIdAndStatus(ownerId, status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectByName(String name) {
        Project project = projectRepository.findByName(name)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Project not found with name: " + name
                        )
                );
        return convertToDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectByNameContaining(String name) {
        return projectRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto requestDto) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Project not found with id: " + id
                        )
                );

        if (requestDto.getName() != null) {
            project.setName(requestDto.getName());
        }

        if (requestDto.getType() != null) {
            project.setType(requestDto.getType());
        }

        if (requestDto.getDescription() != null) {
            project.setDescription(requestDto.getDescription());
        }

        if (requestDto.getStatus() != null) {
            project.setStatus(requestDto.getStatus());
        }

        if (requestDto.getVisibility() != null) {
            project.setVisibility(requestDto.getVisibility());
        }

        if (requestDto.getLatestUpdate() != null) {
            project.setLatestUpdate(requestDto.getLatestUpdate());
        }

        if (requestDto.getMemberCount() >= 0) {
            project.setMemberCount(requestDto.getMemberCount());
        }

        Project updatedProject = projectRepository.save(project);
        return convertToDto(updatedProject);
    }

    @Transactional
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Project not found with id: " + id
                        )
                );

        projectRepository.delete(project);
    }
}
