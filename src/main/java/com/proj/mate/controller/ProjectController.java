package com.proj.mate.controller;

import com.proj.mate.dto.ProjectMemberResponseDto;
import com.proj.mate.dto.ProjectRequestDto;
import com.proj.mate.dto.ProjectResponseDto;
import com.proj.mate.service.ProjectMemberService;
import com.proj.mate.service.ProjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    public ProjectController(ProjectService projectService, ProjectMemberService projectMemberService) {
        this.projectService = projectService;
        this.projectMemberService = projectMemberService;
    }

    // ==================== PROJECT ENDPOINTS ====================

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        ProjectResponseDto project = projectService.getProjectById(id);

        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjectsByOwnerId(
            @RequestParam Long ownerId) {

        return ResponseEntity.ok(
                projectService.getProjectByOwnerId(ownerId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponseDto>> searchProjectsByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                projectService.getProjectByNameContaining(name)
        );
    }

    @GetMapping("/search/type")
    public ResponseEntity<List<ProjectResponseDto>> searchProjectsByType(
            @RequestParam String type) {

        return ResponseEntity.ok(
                projectService.getProjectByType(type)
        );
    }

    @GetMapping("/search/visibility")
    public ResponseEntity<List<ProjectResponseDto>> searchProjectsByVisibility(
            @RequestParam String visibility) {

        return ResponseEntity.ok(
                projectService.getProjectByVisibility(visibility)
        );
    }

    @GetMapping("/search/status")
    public ResponseEntity<List<ProjectResponseDto>> searchProjectsByStatus(
            @RequestParam String status) {

        return ResponseEntity.ok(
                projectService.getProjectByStatus(status)
        );
    }

    @GetMapping("/search/owner-status")
    public ResponseEntity<List<ProjectResponseDto>> searchProjectsByOwnerIdAndStatus(
            @RequestParam Long ownerId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                projectService.getProjectByOwnerIdAndStatus(ownerId, status)
        );
    }

    @GetMapping("/search/exact-name")
    public ResponseEntity<ProjectResponseDto> getProjectByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                projectService.getProjectByName(name)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto createdProject = projectService.createProject(requestDto);
        return ResponseEntity.ok(createdProject);
    }

    @DeleteMapping("/delete/{id:\\d+}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id:\\d+}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto updated = projectService.updateProject(id, requestDto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== PROJECT MEMBER ENDPOINTS ====================

    @PostMapping("/{projectId:\\d+}/members")
    public ResponseEntity<ProjectMemberResponseDto> addMemberToProject(
            @PathVariable Long projectId,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "MEMBER") String role,
            @RequestParam(required = false) String roleInProject) {

        ProjectMemberResponseDto member = projectMemberService.addMemberToProject(projectId, userId, role, roleInProject);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{projectId:\\d+}/members/{userId:\\d+}")
    public ResponseEntity<Void> removeMemberFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectMemberService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId:\\d+}/members")
    public ResponseEntity<List<ProjectMemberResponseDto>> getMembersByProjectId(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                projectMemberService.getMembersByProjectId(projectId)
        );
    }

    @GetMapping("/{projectId:\\d+}/members/check")
    public ResponseEntity<Boolean> isMemberOfProject(
            @PathVariable Long projectId,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                projectMemberService.isMemberOfProject(projectId, userId)
        );
    }

    @GetMapping("/{projectId:\\d+}/owner/check")
    public ResponseEntity<Boolean> isProjectOwner(
            @PathVariable Long projectId,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                projectMemberService.isProjectOwner(projectId, userId)
        );
    }

    @PutMapping("/{projectId:\\d+}/members/{userId:\\d+}/role")
    public ResponseEntity<ProjectMemberResponseDto> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestParam String role) {

        ProjectMemberResponseDto updatedMember = projectMemberService.updateMemberRole(projectId, userId, role);
        return ResponseEntity.ok(updatedMember);
    }

    @PutMapping("/{projectId:\\d+}/members/{userId:\\d+}/role-in-project")
    public ResponseEntity<ProjectMemberResponseDto> updateRoleInProject(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestParam String roleInProject) {

        ProjectMemberResponseDto updatedMember = projectMemberService.updateRoleInProject(projectId, userId, roleInProject);
        return ResponseEntity.ok(updatedMember);
    }

    @GetMapping("/members/user/{userId:\\d+}")
    public ResponseEntity<List<ProjectMemberResponseDto>> getProjectsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                projectMemberService.getProjectsByUserId(userId)
        );
    }
}
