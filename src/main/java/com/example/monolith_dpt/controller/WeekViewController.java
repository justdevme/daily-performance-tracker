package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.dto.HabitWeekResponse;
import com.example.monolith_dpt.dto.TaskWeekResponse;
import com.example.monolith_dpt.security.CurrentUserService;
import com.example.monolith_dpt.security.CurrentUserServiceImpl;
import com.example.monolith_dpt.service.week.HabitQueryService;
import com.example.monolith_dpt.service.week.TaskQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/week")
@RequiredArgsConstructor
public class WeekViewController {

    private final TaskQueryService taskQueryService;
    private final HabitQueryService habitQueryService;
    private final CurrentUserService currentUserService;

    @GetMapping("/tasks")
    public List<TaskWeekResponse> tasks(@RequestParam LocalDate weekStart) {
        Integer userId = currentUserService.getUserId();
        return taskQueryService.getTasksOfWeek(userId, weekStart);
    }

    @GetMapping("/habits")
    public List<HabitWeekResponse> habits(@RequestParam LocalDate weekStart) {
        return habitQueryService.getHabitsOfWeek(weekStart);
    }
}