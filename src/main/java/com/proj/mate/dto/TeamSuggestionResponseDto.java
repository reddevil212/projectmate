package com.proj.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamSuggestionResponseDto {
    private Long projectId;
    private String projectName;
    private int totalRolesRequired;
    private List<RoleMatchRecommendationDto> roleRecommendations;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleMatchRecommendationDto {
        private Long roleId;
        private String roleName;
        private List<UserCandidateMatchDto> topCandidates;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCandidateMatchDto {
        private UserResponseDto candidate;
        private double matchScore; // Percentage score e.g. 87.5%
        private int totalRequiredSkills;
        private int matchedSkillsCount;
        private List<SkillMatchDetailDto> matchedSkills;
        private List<SkillMatchDetailDto> missingSkills;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillMatchDetailDto {
        private String skillName;
        private int userProficiency;
        private int requiredProficiency;
        private boolean isMatched;
    }
}
