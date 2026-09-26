package com.proj.mate.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberResponseDto {
    private Long id;
    private Long projectId;
    private UserResponseDto user;
    private String role;
    private String roleInProject;
    private LocalDateTime assignedAt;
}
