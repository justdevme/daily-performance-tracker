package com.example.monolith_dpt.dto;

import java.time.Instant;

public record StopFocusSessionRequest(
        Instant endedAt,
        String action
) {
}
