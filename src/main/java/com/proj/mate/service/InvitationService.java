package com.proj.mate.service;

import com.proj.mate.dto.InvitationRequestDto;
import com.proj.mate.dto.InvitationResponseDto;
import com.proj.mate.dto.ProjectResponseDto;
import com.proj.mate.dto.UserResponseDto;
import com.proj.mate.entity.Invitation;
import com.proj.mate.entity.Project;
import com.proj.mate.entity.ProjectMember;
import com.proj.mate.entity.UserInfo;
import com.proj.mate.repository.InvitationRepository;
import com.proj.mate.repository.ProjectMemberRepository;
import com.proj.mate.repository.ProjectRepository;
import com.proj.mate.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    public InvitationService(InvitationRepository invitationRepository,
                             UserRepository userRepository,
                             ProjectRepository projectRepository,
                             ProjectMemberRepository projectMemberRepository,
                             NotificationService notificationService,
                             ModelMapper modelMapper) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }

    private InvitationResponseDto convertToDto(Invitation invitation) {
        UserResponseDto senderDto = modelMapper.map(invitation.getUser(), UserResponseDto.class);
        UserResponseDto receiverDto = modelMapper.map(invitation.getReceiver(), UserResponseDto.class);
        ProjectResponseDto projectDto = modelMapper.map(invitation.getProject(), ProjectResponseDto.class);

        return InvitationResponseDto.builder()
                .id(invitation.getId())
                .sender(senderDto)
                .receiver(receiverDto)
                .project(projectDto)
                .status(invitation.getStatus())
                .createdAt(invitation.getCreatedAt())
                .expiresAt(invitation.getExpiresAt())
                .acceptedAt(invitation.getAcceptedAt())
                .rejectedAt(invitation.getRejectedAt())
                .build();
    }

    @Transactional
    public InvitationResponseDto sendInvitation(InvitationRequestDto requestDto) {
        UserInfo sender = userRepository.findById(requestDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found with id: " + requestDto.getSenderId()));

        UserInfo receiver = userRepository.findById(requestDto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found with id: " + requestDto.getReceiverId()));

        Project project = projectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + requestDto.getProjectId()));

        if (projectMemberRepository.existsByProjectIdAndUserId(project.getId(), receiver.getId())) {
            throw new IllegalStateException("User is already a member of this project");
        }

        if (invitationRepository.existsByProjectIdAndReceiverIdAndStatus(project.getId(), receiver.getId(), "PENDING")) {
            throw new IllegalStateException("A pending invitation already exists for this user and project");
        }

        Invitation invitation = new Invitation();
        invitation.setUser(sender);
        invitation.setReceiver(receiver);
        invitation.setProject(project);
        invitation.setStatus("PENDING");
        invitation.setCreatedAt(LocalDateTime.now());
        invitation.setExpiresAt(requestDto.getExpiresAt());

        Invitation saved = invitationRepository.save(invitation);

        // Notify receiver
        notificationService.sendNotification(
                receiver.getId(),
                "You have received a project invitation to join '" + project.getName() + "' from " + sender.getName() + "."
        );

        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<InvitationResponseDto> getInvitationsByReceiver(Long receiverId) {
        return invitationRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvitationResponseDto> getPendingInvitationsByReceiver(Long receiverId) {
        return invitationRepository.findByReceiverIdAndStatusOrderByCreatedAtDesc(receiverId, "PENDING")
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvitationResponseDto> getInvitationsBySender(Long senderId) {
        return invitationRepository.findByUserIdOrderByCreatedAtDesc(senderId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvitationResponseDto> getInvitationsByProject(Long projectId) {
        return invitationRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvitationResponseDto acceptInvitation(Long invitationId, Long receiverId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found with id: " + invitationId));

        if (!invitation.getReceiver().getId().equals(receiverId)) {
            throw new IllegalArgumentException("User is not the receiver of this invitation");
        }

        if (!"PENDING".equalsIgnoreCase(invitation.getStatus())) {
            throw new IllegalStateException("Invitation is not in PENDING state");
        }

        invitation.setStatus("ACCEPTED");
        invitation.setAcceptedAt(LocalDateTime.now());

        // Add user to project members
        if (!projectMemberRepository.existsByProjectIdAndUserId(invitation.getProject().getId(), receiverId)) {
            ProjectMember member = ProjectMember.builder()
                    .project(invitation.getProject())
                    .user(invitation.getReceiver())
                    .role("MEMBER")
                    .assignedAt(LocalDateTime.now())
                    .build();
            projectMemberRepository.save(member);
        }

        Invitation saved = invitationRepository.save(invitation);

        // Notify sender
        notificationService.sendNotification(
                invitation.getUser().getId(),
                invitation.getReceiver().getName() + " accepted your invitation to join '" + invitation.getProject().getName() + "'."
        );

        return convertToDto(saved);
    }

    @Transactional
    public InvitationResponseDto rejectInvitation(Long invitationId, Long receiverId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found with id: " + invitationId));

        if (!invitation.getReceiver().getId().equals(receiverId)) {
            throw new IllegalArgumentException("User is not the receiver of this invitation");
        }

        if (!"PENDING".equalsIgnoreCase(invitation.getStatus())) {
            throw new IllegalStateException("Invitation is not in PENDING state");
        }

        invitation.setStatus("REJECTED");
        invitation.setRejectedAt(LocalDateTime.now());

        Invitation saved = invitationRepository.save(invitation);

        // Notify sender
        notificationService.sendNotification(
                invitation.getUser().getId(),
                invitation.getReceiver().getName() + " declined your invitation to join '" + invitation.getProject().getName() + "'."
        );

        return convertToDto(saved);
    }

    @Transactional
    public InvitationResponseDto cancelInvitation(Long invitationId, Long senderId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found with id: " + invitationId));

        if (!invitation.getUser().getId().equals(senderId)) {
            throw new IllegalArgumentException("User is not the sender of this invitation");
        }

        invitation.setStatus("CANCELLED");
        Invitation saved = invitationRepository.save(invitation);
        return convertToDto(saved);
    }
}
