package com.example.monolith_dpt.dto;

import com.example.monolith_dpt.entity.DayOfWeek;
import com.example.monolith_dpt.entity.Priority;
import com.example.monolith_dpt.entity.TaskStatus;
import java.time.LocalDate;

public record CreateTaskRequest(
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        DayOfWeek dayOfWeek,
        LocalDate dueDate,
        Long weekId
) {
}
