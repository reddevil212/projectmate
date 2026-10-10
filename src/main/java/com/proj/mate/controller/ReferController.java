package com.proj.mate.controller;

import com.proj.mate.dto.ReferRequestDto;
import com.proj.mate.dto.TeamSuggestionResponseDto;
import com.proj.mate.service.ReferService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refer")
public class ReferController {

    private final ReferService referService;

    public ReferController(ReferService referService) {
        this.referService = referService;
    }

    /**
     * Recommends a team for an existing project by evaluating candidate users against
     * the required project roles and skills using a match score algorithm.
     */
    @GetMapping("/suggest-team/{projectId}")
    public ResponseEntity<TeamSuggestionResponseDto> suggestTeamForProject(
            @PathVariable Long projectId) {

        TeamSuggestionResponseDto suggestion = referService.suggestTeamForProject(projectId);
        return ResponseEntity.ok(suggestion);
    }

    /**
     * Recommends a team based on custom or AI-suggested project roles and required skills.
     */
    @PostMapping("/suggest-team")
    public ResponseEntity<TeamSuggestionResponseDto> suggestTeamFromAiAnalysis(
            @RequestBody ReferRequestDto requestDto) {

        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        TeamSuggestionResponseDto suggestion = referService.suggestTeamFromAiAnalysis(requestDto);
        return ResponseEntity.ok(suggestion);
    }

    /**
     * Ranks and matches candidate users specifically for a project role.
     */
    @GetMapping("/match-users-for-role/{roleId}")
    public ResponseEntity<TeamSuggestionResponseDto.RoleMatchRecommendationDto> matchUsersForRole(
            @PathVariable Long roleId,
            @RequestParam(required = false) Long projectId) {

        TeamSuggestionResponseDto.RoleMatchRecommendationDto match = referService.matchUsersForRole(roleId, projectId);
        return ResponseEntity.ok(match);
    }
}
