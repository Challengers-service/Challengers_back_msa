package com.challengers.challengeservice.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserIdHeaderConverter implements Converter<String, Long> {

    @Override
    public Long convert(String userId) {
        return Long.parseLong(userId);
    }
}
