package com.proj.mate.service;

import com.proj.mate.dto.NotificationRequestDto;
import com.proj.mate.dto.NotificationResponseDto;
import com.proj.mate.entity.Notification;
import com.proj.mate.entity.UserInfo;
import com.proj.mate.repository.NotificationRepository;
import com.proj.mate.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private NotificationResponseDto convertToDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .userName(notification.getUser().getName())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .expiresAt(notification.getExpiresAt())
                .readAt(notification.getReadAt())
                .deletedAt(notification.getDeletedAt())
                .build();
    }

    @Transactional
    public NotificationResponseDto createNotification(NotificationRequestDto requestDto) {
        UserInfo user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + requestDto.getUserId()));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(requestDto.getMessage());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setExpiresAt(requestDto.getExpiresAt());

        Notification saved = notificationRepository.save(notification);
        return convertToDto(saved);
    }

    @Transactional
    public NotificationResponseDto sendNotification(Long userId, String message) {
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .userId(userId)
                .message(message)
                .build();
        return createNotification(requestDto);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotificationsByUserId(Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return notificationRepository.findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getUnreadNotificationsByUserId(Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return notificationRepository.findByUserAndIsReadFalseAndDeletedAtIsNullOrderByCreatedAtDesc(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return notificationRepository.countByUserAndIsReadFalseAndDeletedAtIsNull(user);
    }

    @Transactional
    public NotificationResponseDto markAsRead(Long id, Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Notification notification = notificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);
        return convertToDto(saved);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        List<Notification> unreadList = notificationRepository.findByUserAndIsReadFalseAndDeletedAtIsNullOrderByCreatedAtDesc(user);
        LocalDateTime now = LocalDateTime.now();
        for (Notification notification : unreadList) {
            notification.setRead(true);
            notification.setReadAt(now);
        }
        notificationRepository.saveAll(unreadList);
    }

    @Transactional
    public void softDeleteNotification(Long id, Long userId) {
        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Notification notification = notificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));

        notification.setDeletedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }
}
