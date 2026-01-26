package com.blog01.backend.profile.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String about;
    private String profileImage;
    private String email;
    private long nbr_of_posts;
    private long nbr_of_followers;
    private long nbr_of_following;
    private long nbr_of_likes_received;
    private boolean isMyProfile;
    private boolean isFollowing;
}
