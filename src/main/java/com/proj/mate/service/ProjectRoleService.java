package com.proj.mate.service;

import com.proj.mate.entity.ProjectRole;
import com.proj.mate.repository.ProjectRoleRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectRoleService {

    private final ProjectRoleRepository projectRoleRepository;

    public ProjectRoleService(ProjectRoleRepository projectRoleRepository) {
        this.projectRoleRepository = projectRoleRepository;
    }

    // CREATE
    public ProjectRole createProjectRole(String roleName) {

        ProjectRole newRole = ProjectRole.builder()
                .name(roleName)
                .build();

        return projectRoleRepository.save(newRole);
    }

    // GET BY ID
    public Optional<ProjectRole> getProjectRoleById(Long id) {
        return projectRoleRepository.findById(id);
    }

    // GET BY NAME
    public Optional<ProjectRole> getProjectRoleByName(String name) {
        return projectRoleRepository.findByName(name);
    }

    // GET ALL
    public List<ProjectRole> getAllProjectRoles() {
        return projectRoleRepository.findAll();
    }

    // SEARCH BY NAME - CASE INSENSITIVE
    public Optional<ProjectRole> searchProjectRolesByName(String name) {
        return projectRoleRepository.findByNameIgnoreCase(name);
    }

    // UPDATE
    public Optional<ProjectRole> updateProjectRole(Long id, String updatedName) {

        return projectRoleRepository.findById(id)
                .map(role -> {
                    role.setName(updatedName);
                    return projectRoleRepository.save(role);
                });
    }

    // DELETE
    public boolean deleteProjectRole(Long id) {

        if (!projectRoleRepository.existsById(id)) {
            return false;
        }

        projectRoleRepository.deleteById(id);
        return true;
    }
}
