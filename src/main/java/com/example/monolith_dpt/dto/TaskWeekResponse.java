package com.example.monolith_dpt.dto;

import java.time.LocalDate;

public record TaskWeekResponse(
        Long id,
        String title,
        String description,
        LocalDate dueDate,
        String priority,
        String status
) {
}
