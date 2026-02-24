package com.example.monolith_dpt.service.performance;

import com.example.monolith_dpt.entity.Habit;
import com.example.monolith_dpt.entity.performance.HabitCompletion;
import com.example.monolith_dpt.repository.HabitRepository;
import com.example.monolith_dpt.repository.performance.HabitCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HabitCompletionService {

    private final HabitRepository habitRepository;
    private final HabitCompletionRepository completionRepository;

    @Transactional
    public HabitCompletion toggle(Long habitId, LocalDate date) {

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + habitId));

        HabitCompletion hc = completionRepository
                .findByHabitIdAndCompletedDate(habitId, date)
                .orElseGet(() -> HabitCompletion.builder()
                        .habit(habit)
                        .completedDate(date)
                        .completed(false) // default false, rồi toggle thành true
                        .createdAt(Instant.now())
                        .build());

        hc.setCompleted(!hc.isCompleted());

        if (hc.getCreatedAt() == null) {
            hc.setCreatedAt(Instant.now());
        }

        return completionRepository.save(hc);
    }
}