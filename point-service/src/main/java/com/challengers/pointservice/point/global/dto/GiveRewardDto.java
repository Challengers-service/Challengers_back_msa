package com.challengers.pointservice.point.global.dto;

import lombok.Data;

@Data
public class GiveRewardDto {
    private Long userId;
    private Long reward;

    public GiveRewardDto(Long userId, Long reward) {
        this.userId = userId;
        this.reward = reward;
    }
}
