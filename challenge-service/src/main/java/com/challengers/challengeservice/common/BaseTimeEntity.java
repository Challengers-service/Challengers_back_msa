package com.challengers.challengeservice.common;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public abstract class BaseTimeEntity {
    private static final DateTimeFormatter customDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public String getCreatedDateYYYYMMDD() {
        return Objects.requireNonNullElseGet(createdDate, LocalDateTime::now).format(customDateTimeFormatter);
    }
}
