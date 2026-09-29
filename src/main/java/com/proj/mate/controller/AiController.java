package com.proj.mate.controller;

import com.proj.mate.dto.AiProjectAnalysisRequest;
import com.proj.mate.dto.AiProjectAnalysisResponse;
import com.proj.mate.dto.ProjectResponseDto;
import com.proj.mate.service.AiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * Analyze a user's project idea/prompt and extract structured project info,
     * tech stack requirements, and 4 team member roles with required skills.
     */
    @PostMapping("/analyze")
    public ResponseEntity<AiProjectAnalysisResponse> analyzeProject(
            @RequestBody AiProjectAnalysisRequest request) {

        if (request == null || request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AiProjectAnalysisResponse response = aiService.analyzeProject(request.getPrompt());
        return ResponseEntity.ok(response);
    }

    /**
     * Analyze a user's prompt using Google AI Studio Gemini API and automatically
     * create and persist the Project, required Skills, and 4 Project Roles in the database.
     */
    @PostMapping("/create-project")
    public ResponseEntity<ProjectResponseDto> createProjectFromPrompt(
            @RequestParam Long ownerId,
            @RequestBody AiProjectAnalysisRequest request) {

        if (request == null || request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AiProjectAnalysisResponse analysis = aiService.analyzeProject(request.getPrompt());
        ProjectResponseDto createdProject = aiService.createProjectFromAiAnalysis(analysis, ownerId);
        return ResponseEntity.ok(createdProject);
    }

    /**
     * Persist an existing AI project analysis object directly into the database.
     */
    @PostMapping("/save-analysis")
    public ResponseEntity<ProjectResponseDto> saveAnalysis(
            @RequestParam Long ownerId,
            @RequestBody AiProjectAnalysisResponse analysis) {

        if (analysis == null) {
            return ResponseEntity.badRequest().build();
        }

        ProjectResponseDto createdProject = aiService.createProjectFromAiAnalysis(analysis, ownerId);
        return ResponseEntity.ok(createdProject);
    }
}
