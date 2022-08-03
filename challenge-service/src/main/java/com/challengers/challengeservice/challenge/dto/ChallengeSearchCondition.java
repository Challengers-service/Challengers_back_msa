package com.challengers.challengeservice.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChallengeSearchCondition {
    private String category;
    private String challengeName;
    private String tagName;
}
