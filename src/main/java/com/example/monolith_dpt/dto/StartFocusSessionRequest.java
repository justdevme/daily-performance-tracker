package com.example.monolith_dpt.dto;

import java.time.Instant;

public record StartFocusSessionRequest(
        Instant startedAt
) {
}
