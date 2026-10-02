package com.proj.mate.controller;

import com.proj.mate.dto.InvitationRequestDto;
import com.proj.mate.dto.InvitationResponseDto;
import com.proj.mate.service.InvitationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationResponseDto> sendInvitation(
            @RequestBody InvitationRequestDto requestDto) {

        InvitationResponseDto response = invitationService.sendInvitation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<InvitationResponseDto>> getInvitationsByReceiver(
            @PathVariable Long receiverId) {

        return ResponseEntity.ok(invitationService.getInvitationsByReceiver(receiverId));
    }

    @GetMapping("/receiver/{receiverId}/pending")
    public ResponseEntity<List<InvitationResponseDto>> getPendingInvitationsByReceiver(
            @PathVariable Long receiverId) {

        return ResponseEntity.ok(invitationService.getPendingInvitationsByReceiver(receiverId));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<InvitationResponseDto>> getInvitationsBySender(
            @PathVariable Long senderId) {

        return ResponseEntity.ok(invitationService.getInvitationsBySender(senderId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<InvitationResponseDto>> getInvitationsByProject(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(invitationService.getInvitationsByProject(projectId));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<InvitationResponseDto> acceptInvitation(
            @PathVariable Long id,
            @RequestParam Long receiverId) {

        return ResponseEntity.ok(invitationService.acceptInvitation(id, receiverId));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<InvitationResponseDto> rejectInvitation(
            @PathVariable Long id,
            @RequestParam Long receiverId) {

        return ResponseEntity.ok(invitationService.rejectInvitation(id, receiverId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<InvitationResponseDto> cancelInvitation(
            @PathVariable Long id,
            @RequestParam Long senderId) {

        return ResponseEntity.ok(invitationService.cancelInvitation(id, senderId));
    }
}
