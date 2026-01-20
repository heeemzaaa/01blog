package com.blog01.backend.notification.controller;

import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.response.NotificationResponse;
import com.blog01.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ResponseData<List<NotificationResponse>>> getMyNotifications(Principal principal) {
        return ResponseEntity.ok(notificationService.getMyNotifications(principal.getName()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ResponseData<String>> markAsRead(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(notificationService.markAsRead(principal.getName(), id));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ResponseData<String>> markAllAsRead(Principal principal) {
        return ResponseEntity.ok(notificationService.markAllAsRead(principal.getName()));
    }
}