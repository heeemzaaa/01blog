package com.blog01.backend.subscribes.controller;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.subscribes.service.SubscribeService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    /* ===================== GET SUBSCRIBERS ===================== */
    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<ResponseData<List<UserResponse>>> getSubscribers(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(subscribeService.getSubscribers(userId));
    }

    /* ===================== GET SUBSCRIBED ===================== */
    @GetMapping("/subscribed/{userId}")
    public ResponseEntity<ResponseData<List<UserResponse>>> getSubscribed(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(subscribeService.getSubscribed(userId));
    }

    /* ===================== SUBSCRIBE ===================== */
    @PostMapping("/subscribe/{id}")
    public ResponseEntity<ResponseData<Void>> subscribe(
            @PathVariable UUID id,
            Principal principal) {

        return ResponseEntity.ok(
                subscribeService.subscribe(principal.getName(), id));
    }

    /* ===================== UNSUBSCRIBE ===================== */
    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<ResponseData<Void>> unsubscribe(
            @PathVariable UUID id,
            Principal principal) {

        return ResponseEntity.ok(
                subscribeService.unsubscribe(principal.getName(), id));
    }

    /* ===================== DELETE SUBSCRIBER ===================== */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> deleteSubscriber(
            @PathVariable UUID id,
            Principal principal) {

        return ResponseEntity.ok(
                subscribeService.deleteSubscriber(principal.getName(), id));
    }
}
