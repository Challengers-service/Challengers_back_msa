package com.challengers.challengeservice.photocheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckRequest {
    private List<Long> photoCheckIds;
}
