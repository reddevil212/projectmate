package com.proj.mate.controller;

import com.proj.mate.dto.UserSkillRequestDto;
import com.proj.mate.dto.UserSkillResponseDto;
import com.proj.mate.service.UserSkillService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-skills")
public class UserSkillController {

    private final UserSkillService userSkillService;

    public UserSkillController(UserSkillService userSkillService) {
        this.userSkillService = userSkillService;
    }

    @PostMapping
    public ResponseEntity<UserSkillResponseDto> addUserSkill(
            @RequestBody UserSkillRequestDto requestDto) {

        UserSkillResponseDto response = userSkillService.addUserSkill(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSkillResponseDto> getUserSkillById(@PathVariable Long id) {
        return userSkillService.getUserSkillById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSkillResponseDto>> getUserSkillsByUserId(
            @PathVariable Long userId) {

        List<UserSkillResponseDto> userSkills = userSkillService.getUserSkillsByUserId(userId);
        return ResponseEntity.ok(userSkills);
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<UserSkillResponseDto>> getUsersBySkillId(
            @PathVariable Long skillId) {

        List<UserSkillResponseDto> userSkills = userSkillService.getUsersBySkillId(skillId);
        return ResponseEntity.ok(userSkills);
    }

    @GetMapping("/skill/{skillId}/min-proficiency/{minProficiency}")
    public ResponseEntity<List<UserSkillResponseDto>> getUsersBySkillAndMinProficiency(
            @PathVariable Long skillId,
            @PathVariable int minProficiency) {

        List<UserSkillResponseDto> userSkills = userSkillService.getUsersBySkillAndMinProficiency(skillId, minProficiency);
        return ResponseEntity.ok(userSkills);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSkillResponseDto> updateUserSkillProficiency(
            @PathVariable Long id,
            @RequestParam int proficiency) {

        return userSkillService.updateUserSkillProficiency(id, proficiency)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSkill(@PathVariable Long id) {
        if (userSkillService.deleteUserSkill(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{userId}/skill/{skillId}")
    public ResponseEntity<Void> deleteUserSkillByUserIdAndSkillId(
            @PathVariable Long userId,
            @PathVariable Long skillId) {

        if (userSkillService.deleteUserSkillByUserIdAndSkillId(userId, skillId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
