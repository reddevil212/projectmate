package com.proj.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationRequestDto {
    private Long senderId;
    private Long receiverId;
    private Long projectId;
    private LocalDateTime expiresAt;
}
