package com.blog01.backend.notification.service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.repository.NotificationRepository;
import com.blog01.backend.notification.response.NotificationResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void sendNotification(User recipient, User actor, NotificationType type, UUID relatedId) {
        if (recipient.getId().equals(actor.getId())) {
            return;
        }

        Notification notification = Notification.builder()
                .recipient(recipient)
                .actor(actor)
                .type(type)
                .relatedEntityId(relatedId)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    public ResponseData<List<NotificationResponse>> getMyNotifications(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Notification> list = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);

        List<NotificationResponse> response = list.stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());

        return ResponseData.success("Notifications fetched", response);
    }

    public ResponseData<String> markAsRead(String email, UUID notificationId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            return ResponseData.error("You are not authorized to access this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);

        return ResponseData.success("Notification marked as read", null);
    }

    public ResponseData<String> markAllAsRead(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Notification> unread = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        
        for (Notification n : unread) {
            if (!n.isRead()) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        }
        return ResponseData.success("All notifications marked as read", null);
    }

    private NotificationResponse mapToNotificationResponse(Notification n) {
        UserResponse actorResponse = null;
        if (n.getActor() != null) {
            actorResponse = UserResponse.builder()
                    .id(n.getActor().getId())
                    .firstName(n.getActor().getFirstName())
                    .lastName(n.getActor().getLastName())
                    .username(n.getActor().getUsername())
                    .profileImage(n.getActor().getProfileImage())
                    .build();
        }

        return NotificationResponse.builder()
                .id(n.getId())
                .actor(actorResponse)
                .type(n.getType())
                .relatedEntityId(n.getRelatedEntityId())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
