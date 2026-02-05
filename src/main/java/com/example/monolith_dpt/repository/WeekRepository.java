package com.example.monolith_dpt.repository;

import com.example.monolith_dpt.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeekRepository extends JpaRepository<Week, Long> {
    List<Week> findByMonthAndYearOrderByWeekNumberAsc(Integer month, Integer year);
    boolean existsByMonthAndYearAndWeekNumber(Integer month, Integer year, Integer weekNumber);
    Optional <Week> findById (UUID weekId);
    Optional <Week> findByWeekNumber (Integer weekNumber);
}
