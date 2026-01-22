package com.blog01.backend.auth.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeResponse {
    private String firstName;
    private String lastName;
    private String username;
    private long nbr_of_posts;
    private long nbr_of_followers;
    private long nbr_of_following;
    private long nbr_of_notifications;
    private String profileImage;
}
