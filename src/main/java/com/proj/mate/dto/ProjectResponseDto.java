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
public class ProjectResponseDto {
    private Long id;
    private UserResponseDto owner;
    private String name;
    private String type;
    private String description;
    private int memberCount;
    private LocalDateTime createdAt;
    private String status;
}
