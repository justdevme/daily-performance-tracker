package com.example.monolith_dpt.repository.performance;

import com.example.monolith_dpt.entity.performance.HabitCompletion;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
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

    @Query("""
        select hc from HabitCompletion hc
        where hc.habit.user.id = :userId
        and hc.completedDate between :start and :end
    """)
    List<HabitCompletion> findByUserIdAndDateBetween(
            Long userId, LocalDate start, LocalDate end
    );
}