package com.blog01.backend.auth.response;

import java.util.UUID;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.model.User.Role;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private boolean active;
    private long nbr_of_posts;
    private long nbr_of_followers;
    private long nbr_of_following;
    private long nbr_of_notifications;
    private String profileImage;
    private String token;
    private Role role;

    public UserResponse(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.profileImage = user.getProfileImage();
}

}
