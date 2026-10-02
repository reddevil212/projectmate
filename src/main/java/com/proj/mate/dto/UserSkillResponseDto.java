package com.proj.mate.dto;

import com.proj.mate.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillResponseDto {
    private Long id;
    private Long userId;
    private Skill skill;
    private int proficiency;
}
