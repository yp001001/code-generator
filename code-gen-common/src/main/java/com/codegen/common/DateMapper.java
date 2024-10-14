package com.codegen.common;

import java.time.Instant;
import java.util.Objects;

public class DateMapper {
    public Long asLong(Instant date) {
        if (Objects.nonNull(date)) {
            return date.toEpochMilli();
        }
        return null;
    }

    public Instant asInstant(Long date) {
        if (Objects.nonNull(date)) {
            return Instant.ofEpochMilli(date);
        }
        return null;
    }
}