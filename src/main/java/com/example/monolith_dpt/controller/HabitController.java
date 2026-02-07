package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.dto.CreateHabitRequest;
import com.example.monolith_dpt.entity.Habit;
import com.example.monolith_dpt.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/habit")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public Habit createHabit(@RequestBody CreateHabitRequest req) {
        return habitService.createHabit(req);
    }

    @GetMapping
    public List<Habit> list() {
        return habitService.list();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

}
