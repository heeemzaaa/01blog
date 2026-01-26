package com.blog01.backend.profile.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.post.repository.PostRepository;
import com.blog01.backend.post.repository.LikeRepository;
import com.blog01.backend.profile.response.ProfileResponse;
import com.blog01.backend.subscribes.repository.SubscribesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final SubscribesRepository subscribesRepository;

    public ResponseData<ProfileResponse> getProfile(UUID userId, Authentication authentication) {

        User profileUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User viewer = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Viewer not found"));

        boolean isMyProfile = viewer.getId().equals(profileUser.getId());
        boolean isFollowing = false;

        if (!isMyProfile) {
            isFollowing = subscribesRepository
                    .existsBySubscriberAndUser(viewer, profileUser);
        }

        long nbrOfPosts = postRepository.countByUser(profileUser);
        long nbrOfFollowers = subscribesRepository.countByUser(profileUser);
        long nbrOfFollowing = subscribesRepository.countBySubscriber(profileUser);
        long nbrOfLikesReceived = likeRepository.countLikesReceivedByUser(profileUser);

        ProfileResponse response = ProfileResponse.builder()
                .id(profileUser.getId())
                .username(profileUser.getUsername())
                .firstName(profileUser.getFirstName())
                .lastName(profileUser.getLastName())
                .about(profileUser.getAbout())
                .email(profileUser.getEmail())
                .profileImage(profileUser.getProfileImage())
                .nbr_of_posts(nbrOfPosts)
                .nbr_of_followers(nbrOfFollowers)
                .nbr_of_following(nbrOfFollowing)
                .nbr_of_likes_received(nbrOfLikesReceived)
                .isMyProfile(isMyProfile)
                .isFollowing(isFollowing)
                .build();

        return ResponseData.success("Profile fetched successfully", response);
    }
}
