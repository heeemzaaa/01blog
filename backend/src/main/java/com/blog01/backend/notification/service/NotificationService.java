package com.blog01.backend.notification.service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


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

    public ResponseData<List<Notification>> getMyNotifications(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Notification> list = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        return ResponseData.success("Notifications fetched", list);
    }

    public ResponseData<String> markAsRead(UUID notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found !"));
        n.setRead(true);
        notificationRepository.save(n);
        return ResponseData.success("Marked as read", null);
    }
}