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
public class ReferRequestDto {
    private Long projectId;
    private List<CustomRoleRequirement> customRoles;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomRoleRequirement {
        private String roleName;
        private List<CustomSkillRequirement> requiredSkills;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomSkillRequirement {
        private String skillName;
        private int proficiencyRequired; // e.g. 1 - 5
    }
}
