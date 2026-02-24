package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.entity.performance.HabitCompletion;
import com.example.monolith_dpt.service.performance.HabitCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habits")
public class HabitCompletionController {

    private final HabitCompletionService habitCompletionService;

    // Tick/untick completion cho 1 habit tại 1 ngày
    @PostMapping("/{habitId}/toggle")
    public HabitCompletion toggleHabitCompletion(
            @PathVariable Long habitId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return habitCompletionService.toggle(habitId, date);
    }
}