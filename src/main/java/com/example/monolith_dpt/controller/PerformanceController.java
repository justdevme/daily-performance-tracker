package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.dto.PerformanceDailyResponse;
import com.example.monolith_dpt.service.performance.PerformanceEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceEngine engine;

    @GetMapping("/daily")
    public PerformanceDailyResponse daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return engine.calculate(date);
    }
}