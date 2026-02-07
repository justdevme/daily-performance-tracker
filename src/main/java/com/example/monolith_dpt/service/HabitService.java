package com.example.monolith_dpt.service;

import com.example.monolith_dpt.dto.CreateHabitRequest;
import com.example.monolith_dpt.entity.Habit;
import com.example.monolith_dpt.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;

    public Habit createHabit (CreateHabitRequest req) {
        Habit habit = Habit.builder().title(req.title()).build();

        return habitRepository.save(habit);
    }

    public List<Habit> list() {
        return habitRepository.findAll();
    }

    public void deleteHabit (Long habitId) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new RuntimeException("Habit not found"));
        habitRepository.delete(habit);
    }
}
