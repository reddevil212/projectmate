package com.proj.mate.controller;

import com.proj.mate.dto.SkillRequest;
import com.proj.mate.entity.Skill;
import com.proj.mate.service.SkillService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Skill> createSkill(
            @RequestBody SkillRequest request) {

        Skill createdSkill =
                skillService.createSkill(request.getName());

        return ResponseEntity.ok(createdSkill);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {

        return ResponseEntity.ok(
                skillService.getAllSkills()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(
            @PathVariable Long id) {

        return skillService.getSkillById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // GET BY NAME
    @GetMapping("/search")
    public ResponseEntity<Skill> getSkillByName(
            @RequestParam String name) {

        return skillService.searchSkillsByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillRequest request) {

        return skillService.updateSkill(
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
    public ResponseEntity<Void> deleteSkill(
            @PathVariable Long id) {

        if (skillService.deleteSkill(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


}