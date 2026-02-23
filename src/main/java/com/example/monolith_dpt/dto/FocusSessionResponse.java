package com.example.monolith_dpt.dto;

import com.example.monolith_dpt.entity.FocusSessionStatus;

import java.time.Instant;

public record FocusSessionResponse(
        Long id,
        Long taskId,
        FocusSessionStatus status,
        Instant startedAt,
        Instant endedAt,
        Long durationSeconds
) {
}
