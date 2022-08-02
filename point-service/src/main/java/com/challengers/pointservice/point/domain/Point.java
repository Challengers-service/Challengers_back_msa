package com.challengers.pointservice.point.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
    @Id @GeneratedValue
    @Column(name = "point_id")
    private Long id;

    private Long userId;
    private Long point;

    @Builder
    public Point(Long id, Long userId, Long point) {
        this.id = id;
        this.userId = userId;
        this.point = point;
    }

    private Point(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public static Point create(Long userId) {
        return new Point(userId, 0L);
    }

    public void updatePoint(Long pointHistory) {
        point += pointHistory;
    }
}
