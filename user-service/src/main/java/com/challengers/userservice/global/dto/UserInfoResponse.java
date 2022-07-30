package com.challengers.userservice.global.dto;

import com.challengers.userservice.domain.user.entity.User;
import lombok.Data;

@Data
public class UserInfoResponse {
    private Long id;
    private String name;
    private String imageUrl;

    private UserInfoResponse(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getImage()
        );
    }
}
