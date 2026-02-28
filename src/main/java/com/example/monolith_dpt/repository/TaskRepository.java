package com.example.monolith_dpt.repository;

import com.example.monolith_dpt.entity.Task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.monolith_dpt.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByWeekId(Long weekId);
    List<Task> findByDayOfWeekAndWeekId(DayOfWeek day, Long weekId);
    List<Task> findByWeekIdOrderByDueDateAsc(Long weekId);
    long countByStatusAndDueDate(TaskStatus status, LocalDate dueDate);
    List<Task> findByUserIdAndDueDateBetweenOrderByDueDateAsc(
            Integer userId, LocalDate start, LocalDate end
    );
}

