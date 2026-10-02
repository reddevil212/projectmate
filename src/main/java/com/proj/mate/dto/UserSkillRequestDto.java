package com.proj.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillRequestDto {
    private Long userId;
    private Long skillId;
    private String skillName;
    private int proficiency;
}
