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
public class AiProjectAnalysisResponse {
    private String name;
    private String type;
    private String description;
    private int memberCount;
    private String status;
    private List<ProjectSkillRequirementDto> projectSkills;
    private List<RoleRequirementDto> roles;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectSkillRequirementDto {
        private String skillName;
        private String level; // "Beginner", "Intermediate", "Expert"
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleRequirementDto {
        private String roleName;
        private List<RoleSkillRequirementDto> skills;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleSkillRequirementDto {
        private String skillName;
        private int proficiencyRequired; // e.g. 1 - 5
    }
}
