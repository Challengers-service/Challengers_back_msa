package com.challengers.pointservice.point.domain;

import java.util.Arrays;

public enum PointHistoryType {
    DEPOSIT,ATTENDANCE,SUCCESS,CANCEL;

    public static PointHistoryType of(String pointHistoryTypeStr) {
        return Arrays.stream(PointHistoryType.values())
                .filter(checkFrequencyType -> checkFrequencyType.name().equals(pointHistoryTypeStr))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
