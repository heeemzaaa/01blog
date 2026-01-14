package com.blog01.backend.subscribes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.subscribes.service.SubscribeService;

import lombok.RequiredArgsConstructor;
import java.security.Principal;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/subscribers")
    public ResponseEntity<ResponseData<List<UserResponse>>> getSubscribers(Principal principal) {
        return ResponseEntity.ok(subscribeService.getSubscribers(principal.getName()));
    }

    @GetMapping("/subscribed")
    public ResponseEntity<ResponseData<List<UserResponse>>> getSubscribed(Principal principal) {
        return ResponseEntity.ok(subscribeService.getSubscribed(principal.getName()));
    }
    
    @PostMapping("subscribe/{id}")
    public ResponseEntity<ResponseData<String>> subscribe(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(subscribeService.subscribe(principal.getName(), id));
    }

    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<ResponseData<String>> unsubscribe(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(subscribeService.unsubscribe(principal.getName(), id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<String>> deleteSubscriber(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(subscribeService.deleteSubscriber(principal.getName(), id));
    }
    
}
