package com.example.monolith_dpt.repository.performance;

import com.example.monolith_dpt.entity.performance.HabitCompletion;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long> {

    Optional<HabitCompletion> findByHabitIdAndCompletedDate(Long habitId, LocalDate completedDate);

    @Query("""
        select count(hc)
        from HabitCompletion hc
        where hc.completedDate = :date
          and hc.completed = true
    """)
    long countCompletedOnDate(@Param("date") LocalDate date);
}