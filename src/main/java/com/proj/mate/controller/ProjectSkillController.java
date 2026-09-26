package com.proj.mate.controller;

import com.proj.mate.dto.ProjectSkillRequestDto;
import com.proj.mate.dto.ProjectSkillResponseDto;
import com.proj.mate.service.ProjectSkillService;

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
@RequestMapping("/api/project-skills")
public class ProjectSkillController {

    private final ProjectSkillService projectSkillService;

    public ProjectSkillController(ProjectSkillService projectSkillService) {
        this.projectSkillService = projectSkillService;
    }

    @PostMapping
    public ResponseEntity<ProjectSkillResponseDto> addSkillToProject(
            @RequestBody ProjectSkillRequestDto requestDto) {

        ProjectSkillResponseDto response = projectSkillService.addSkillToProject(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSkillResponseDto> getProjectSkillById(
            @PathVariable Long id) {

        return projectSkillService.getProjectSkillById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectSkillResponseDto>> getSkillsByProjectId(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(projectSkillService.getSkillsByProjectId(projectId));
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<ProjectSkillResponseDto>> getProjectsBySkillId(
            @PathVariable Long skillId) {

        return ResponseEntity.ok(projectSkillService.getProjectsBySkillId(skillId));
    }

    @GetMapping("/project/{projectId}/level/{level}")
    public ResponseEntity<List<ProjectSkillResponseDto>> getSkillsByProjectIdAndLevel(
            @PathVariable Long projectId,
            @PathVariable String level) {

        return ResponseEntity.ok(projectSkillService.getSkillsByProjectIdAndLevel(projectId, level));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectSkillResponseDto> updateProjectSkillLevel(
            @PathVariable Long id,
            @RequestParam String level) {

        return projectSkillService.updateProjectSkillLevel(id, level)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectSkill(@PathVariable Long id) {
        if (projectSkillService.deleteProjectSkill(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/project/{projectId}/skill/{skillId}")
    public ResponseEntity<Void> deleteSkillFromProject(
            @PathVariable Long projectId,
            @PathVariable Long skillId) {

        if (projectSkillService.deleteSkillFromProject(projectId, skillId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
