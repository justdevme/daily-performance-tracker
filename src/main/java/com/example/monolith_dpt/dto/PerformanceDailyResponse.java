package com.example.monolith_dpt.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Map;

@Builder
public record PerformanceDailyResponse(
        LocalDate date,

        long doneTasks,
        long focusMinutes,
        long totalHabits,
        long completedHabits,
        double habitCompletionRate,

        double taskScore,
        double habitScore,
        double focusScore,

        Map<String, Double> weights, // debug
        double finalScore
) {}