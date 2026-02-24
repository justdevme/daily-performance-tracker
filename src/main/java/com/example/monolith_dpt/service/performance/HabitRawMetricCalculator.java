package com.example.monolith_dpt.service.performance;

import com.example.monolith_dpt.repository.HabitRepository;
import com.example.monolith_dpt.repository.performance.HabitCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HabitRawMetricCalculator {

    private final HabitRepository habitRepository;
    private final HabitCompletionRepository completionRepository;

    public HabitRawResult completionRate(LocalDate date) {
        long totalHabits = habitRepository.count();
        long completedHabits = completionRepository.countCompletedOnDate(date);

        double rate = (totalHabits == 0) ? 0.0 : (double) completedHabits / totalHabits;

        return new HabitRawResult(totalHabits, completedHabits, rate);
    }

    public record HabitRawResult(long totalHabits, long completedHabits, double completionRate) {}
}