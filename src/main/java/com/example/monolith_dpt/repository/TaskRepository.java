package com.example.monolith_dpt.repository;

import com.example.monolith_dpt.entity.Task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByWeekId(Long weekId);
    List<Task> findByDayOfWeekAndWeekId(DayOfWeek day, Long weekId);
    List<Task> findByWeekIdOrderByDueDateAsc(Long weekId);
}

