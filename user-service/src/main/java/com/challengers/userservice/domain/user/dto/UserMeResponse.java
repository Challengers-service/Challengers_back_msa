package com.challengers.userservice.domain.user.dto;

import com.challengers.userservice.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMeResponse {
    private Long id;
    private String email;
    private String name;
    private String image;
    private String bio;

    @Builder
    public UserMeResponse(User user, Long followerCount, Long followingCount) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.image = user.getImage();
        this.bio = user.getBio();
    }
}
