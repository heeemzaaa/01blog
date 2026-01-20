package com.blog01.backend.notification.response;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.notification.model.Notification.NotificationType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {
    private UUID id;
    private UserResponse actor; 
    private NotificationType type;
    private UUID relatedEntityId;
    private boolean isRead;
    private LocalDateTime createdAt;
}