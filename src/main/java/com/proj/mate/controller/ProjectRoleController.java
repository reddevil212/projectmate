package com.proj.mate.controller;

import com.proj.mate.dto.ProjectRoleRequest;
import com.proj.mate.entity.ProjectRole;
import com.proj.mate.service.ProjectRoleService;

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
@RequestMapping("/api/project-roles")
public class ProjectRoleController {

    private final ProjectRoleService projectRoleService;

    public ProjectRoleController(ProjectRoleService projectRoleService) {
        this.projectRoleService = projectRoleService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ProjectRole> createProjectRole(
            @RequestBody ProjectRoleRequest request) {

        ProjectRole createdRole =
                projectRoleService.createProjectRole(request.getName());

        return ResponseEntity.ok(createdRole);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ProjectRole>> getAllProjectRoles() {

        return ResponseEntity.ok(
                projectRoleService.getAllProjectRoles()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectRole> getProjectRoleById(
            @PathVariable Long id) {

        return projectRoleService.getProjectRoleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // GET BY NAME
    @GetMapping("/search")
    public ResponseEntity<ProjectRole> getProjectRoleByName(
            @RequestParam String name) {

        return projectRoleService.searchProjectRolesByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProjectRole> updateProjectRole(
            @PathVariable Long id,
            @RequestBody ProjectRoleRequest request) {

        return projectRoleService.updateProjectRole(
                        id,
                        request.getName()
                )
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectRole(
            @PathVariable Long id) {

        if (projectRoleService.deleteProjectRole(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
