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
public class ProjectSkillResponseDto {

    private Long id;
    private Long projectId;
    private Skill skill;
    private String level;
}
