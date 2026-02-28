package com.example.monolith_dpt.service.week;

import com.example.monolith_dpt.dto.TaskWeekResponse;
import com.example.monolith_dpt.repository.TaskRepository;
import com.example.monolith_dpt.security.CurrentUserService;
import com.example.monolith_dpt.util.WeekUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskQueryService {

    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;

    @Cacheable(
            cacheNames = "tasks_week",
            key = "T(String).format('tasksWeek:%s:%s', #userId, T(com.example.monolith_dpt.util.WeekUtil).normalizeToMonday(#weekStartInput))"
    )
    public List<TaskWeekResponse> getTasksOfWeek(Integer userId, LocalDate weekStartInput) {

        LocalDate weekStart = WeekUtil.normalizeToMonday(weekStartInput);
        LocalDate weekEnd = WeekUtil.weekEnd(weekStart);

        return taskRepository
                .findByUserIdAndDueDateBetweenOrderByDueDateAsc(userId, weekStart, weekEnd)
                .stream()
                .map(t -> new TaskWeekResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getDueDate(),
                        t.getPriority() != null ? t.getPriority().name() : null,
                        t.getStatus() != null ? t.getStatus().name() : null
                ))
                .toList();
    }
}
