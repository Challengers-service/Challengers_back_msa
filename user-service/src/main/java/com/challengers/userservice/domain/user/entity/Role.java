package com.challengers.userservice.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 유저"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String value;
}
