package com.example.app5.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;

public enum Git {
    GITHUB((byte) 0),
    GITLAB((byte) 1);

    private final byte value;

    Git(byte value) {
        this.value=value;
    }

    @JsonValue
    public byte value() {
        return value;
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(super.ordinal());
    }
}
