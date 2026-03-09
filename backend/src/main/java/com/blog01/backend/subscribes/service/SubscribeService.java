package com.blog01.backend.subscribes.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import java.util.*;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.service.NotificationService;
import com.blog01.backend.subscribes.model.Subscribe;
import com.blog01.backend.subscribes.repository.SubscribesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeService {

        private final SubscribesRepository sr;
        private final UserRepository ur;
        private final NotificationService us;

        /* ===================== GET SUBSCRIBERS ===================== */
        public ResponseData<List<UserResponse>> getSubscribers(UUID userId) {

                User user = ur.findById(userId)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));

                if (!user.isActive()) {
                        throw new BadCredentialsException("You are banned !");
                }

                List<UserResponse> subscribers = sr.findByUser(user).stream()
                                .map(sub -> mapToUserResponse(sub.getSubscriber()))
                                .toList();

                return ResponseData.success("Subscribers fetched successfully", subscribers);
        }

        /* ===================== GET SUBSCRIBED ===================== */
        public ResponseData<List<UserResponse>> getSubscribed(UUID userId) {

                User user = ur.findById(userId)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));

                if (!user.isActive()) {
                        throw new BadCredentialsException("You are banned !");
                }

                List<UserResponse> subscribed = sr.findBySubscriber(user).stream()
                                .map(sub -> mapToUserResponse(sub.getUser()))
                                .toList();

                return ResponseData.success("Subscribed users fetched successfully", subscribed);
        }

        /* ===================== SUBSCRIBE ===================== */
        public ResponseData<Void> subscribe(String email, UUID targetUserId) {

                User subscriber = ur.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("user not found"));

                if (!subscriber.isActive()) {
                        throw new BadCredentialsException("You are banned !");
                }

                User target = ur.findById(targetUserId)
                                .orElseThrow(() -> new NoSuchElementException("User to subscribe doesn't exist"));

                if (subscriber.getId().equals(target.getId())) {
                        throw new IllegalArgumentException("You cannot subscribe to yourself");
                }

                if (sr.existsBySubscriberAndUser(subscriber, target)) {
                        throw new IllegalArgumentException("You are already a subscriber to this user");
                }

                sr.save(Subscribe.builder()
                                .subscriber(subscriber)
                                .user(target)
                                .build());

                us.sendNotification(target, subscriber, NotificationType.FOLLOW, targetUserId);

                return ResponseData.success("You are now a subscriber to this user", null);
        }

        /* ===================== UNSUBSCRIBE ===================== */
        public ResponseData<Void> unsubscribe(String email, UUID targetUserId) {

                User user = ur.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("user not found"));

                if (!user.isActive()) {
                        throw new BadCredentialsException("You are banned !");
                }

                User target = ur.findById(targetUserId)
                                .orElseThrow(() -> new NoSuchElementException("User to unsubscribe doesn't exist"));

                if (user.getId().equals(target.getId())) {
                        throw new IllegalArgumentException("You cannot unsubscribe from yourself");
                }

                Subscribe subscribe = sr.findBySubscriberAndUser(user, target)
                                .orElseThrow(() -> new NoSuchElementException("You are not a subscriber to this user"));

                sr.delete(subscribe);

                return ResponseData.success("You are now unsubscribed from this user", null);
        }

        /* ===================== DELETE SUBSCRIBER ===================== */
        public ResponseData<Void> deleteSubscriber(String email, UUID deletedUserId) {

                User user = ur.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("user not found"));

                if (!user.isActive()) {
                        throw new BadCredentialsException("You are banned !");
                }

                User deletedUser = ur.findById(deletedUserId)
                                .orElseThrow(() -> new NoSuchElementException("User to remove doesn't exist"));

                if (user.getId().equals(deletedUser.getId())) {
                        throw new IllegalArgumentException("You don't have any relation with yourself");
                }

                Subscribe subscribe = sr.findBySubscriberAndUser(deletedUser, user)
                                .orElseThrow(() -> new NoSuchElementException("This user is not your subscriber"));

                sr.delete(subscribe);

                return ResponseData.success("Subscriber removed successfully", null);
        }

        /* ===================== MAPPER ===================== */
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
