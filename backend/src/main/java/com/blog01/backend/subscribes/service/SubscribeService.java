package com.blog01.backend.subscribes.service;

import org.springframework.stereotype.Service;
import java.util.*;

import com.blog01.backend.auth.model.*;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.subscribes.model.Subscribe;
import com.blog01.backend.subscribes.repository.SubscribesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribesRepository sr;
    private final UserRepository ur;

    public ResponseData<List<UserResponse>> getSubscribers(UUID userId) {
        User user = ur.findById(userId).orElseThrow();

        List<Subscribe> subscribersList = sr.findByUser(user);

        List<UserResponse> subscribers = subscribersList.stream()
                .map(sub -> mapToUserResponse(sub.getSubscriber()))
                .toList();

        return ResponseData.success("Subscribers fetched successfully", subscribers);
    }

    public ResponseData<List<UserResponse>> getSubscribed(UUID userId) {
        User user = ur.findById(userId).orElseThrow();

        List<Subscribe> subscribedList = sr.findBySubscriber(user);

        List<UserResponse> subscribed = subscribedList.stream()
                .map(sub -> mapToUserResponse(sub.getUser()))
                .toList();

        return ResponseData.success("Subscribed users are fetched successfully", subscribed);
    }

    public ResponseData<String> subscribe(String email, UUID targetUserId) {
        User subscriber = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("Current user not found"));
        // I need to check if the user exists
        User target = ur.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User to subscribe doesn't exist"));
        // I need to check if the user is me (if yes refuse)
        if (subscriber.getId().equals(target.getId())) {
            return ResponseData.error("You cannot subscribe to yourself !");
        }

        // I need to check if I'm already a follower
        boolean exists = sr.existsBySubscriberAndUser(subscriber, target);
        if (exists) {
            return ResponseData.error("You are already a subscriber to this user !");
        }

        Subscribe subscribe = Subscribe.builder()
                .subscriber(subscriber)
                .user(target)
                .build();

        sr.save(subscribe);

        return ResponseData.success("You are now a subscriber to this user", null);

    }

    public ResponseData<String> unsubscribe(String email, UUID targetUserId) {
        User user = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("Current user not found"));

        User target = ur.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User to unsubscribe doesn't exist"));
        // I need to check if the user is me (if yes refuse)
        if (user.getId().equals(target.getId())) {
            return ResponseData.error("You cannot unsubscribe to yourself !");
        }

        Subscribe subscribe = sr.findBySubscriberAndUser(user, target)
                .orElseThrow(() -> new RuntimeException("You are not a subscriber to this user !"));

        sr.delete(subscribe);

        return ResponseData.success("You are now not a subscriber to this user", null);

    }

    public ResponseData<String> deleteSubscriber(String email, UUID deletedUserId) {
        User user = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("Current user not found"));
        User deletedUser = ur.findById(deletedUserId)
                .orElseThrow(() -> new RuntimeException("User to delete from you subscribers doesn't exist"));

        if (user.getId().equals(deletedUser.getId())) {
            return ResponseData.error("You don't have any relation with yourself !");
        }

        Subscribe subscribe = sr.findBySubscriberAndUser(deletedUser, user)
                .orElseThrow(() -> new RuntimeException("You are not a subscriber to this user !"));

        sr.delete(subscribe);

        return ResponseData.success("You deleted the user from your list successfully", null);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .build();
    }

}
