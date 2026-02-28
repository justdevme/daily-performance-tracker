package com.example.monolith_dpt.dto;

import java.time.LocalDate;
import java.util.Map;

public record HabitWeekResponse(
        Long id,
        String title,
        Map<LocalDate, Boolean> completedByDate
) {
}
