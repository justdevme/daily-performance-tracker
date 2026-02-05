package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.entity.Week;
import com.example.monolith_dpt.service.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/weeks")
@RequiredArgsConstructor
public class WeekController {
    private final WeekService weekService;

    @PostMapping("/seed")
    public List<Week> seed (@RequestParam int month, @RequestParam int year) {
        return weekService.seedMonth(month, year);
    }

    @GetMapping
    public List<Week> list(@RequestParam int month, @RequestParam int year) {
        return weekService.list(month, year);
    }
}
