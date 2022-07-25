package com.challengers.challengeservice.challenge.dto;

import lombok.Data;

@Data
public class ChallengeSearchCondition {
    private String category;
    private String challengeName;
    private String tagName;
}
