package com.challengers.pointservice.point.domain;

import java.util.Arrays;

public enum PointTransactionType {
    DEPOSIT,ATTENDANCE,SUCCESS,CANCEL;

    public static PointTransactionType of(String pointHistoryTypeStr) {
        return Arrays.stream(PointTransactionType.values())
                .filter(checkFrequencyType -> checkFrequencyType.name().equals(pointHistoryTypeStr))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
