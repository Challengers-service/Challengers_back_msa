package com.challengers.pointservice.point.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction {
    @Id @GeneratedValue
    @Column(name = "point_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point")
    private Point point;

    private Long pointHistory;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PointTransactionType type;

    public PointTransaction(Point point, Long pointHistory, PointTransactionType type) {
        this.point = point;
        this.pointHistory = pointHistory;
        this.type = type;
    }
}
