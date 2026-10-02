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
public class ProjectRequestDto {
    private Long ownerId;
    private String name;
    private String type;
    private String description;
    private int memberCount;

    @Builder.Default
    private String visibility = "PUBLIC";

    private String latestUpdate;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private String status = "OPEN";
}
