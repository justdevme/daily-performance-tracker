package com.example.monolith_dpt.service.week;

import com.example.monolith_dpt.dto.HabitWeekResponse;
import com.example.monolith_dpt.entity.Habit;
import com.example.monolith_dpt.entity.performance.HabitCompletion;
import com.example.monolith_dpt.repository.HabitRepository;
import com.example.monolith_dpt.repository.performance.HabitCompletionRepository;
import com.example.monolith_dpt.security.CurrentUserService;
import com.example.monolith_dpt.util.WeekUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HabitQueryService {

    private final HabitRepository habitRepository;
    private final HabitCompletionRepository completionRepository;
    private final CurrentUserService currentUserService;

    @Cacheable(
            cacheNames = "habits_week",
            key = "T(String).format('habitsWeek:%s:%s', #userId, T(com.example.monolith_dpt.util.WeekUtil).normalizeToMonday(#weekStartInput))"
    )

    public List<HabitWeekResponse> getHabitsOfWeek(LocalDate weekStartInput) {
        Integer userId = currentUserService.getUserId();

        LocalDate weekStart = WeekUtil.normalizeToMonday(weekStartInput);
        LocalDate weekEnd = WeekUtil.weekEnd(weekStart);

        List<Habit> habits = habitRepository.findByUserId(userId.longValue());

        List<HabitCompletion> completions =
                completionRepository.findByUserIdAndDateBetween(userId.longValue(), weekStart, weekEnd);

        //habitId -> (date -> completed)
        Map<Long, Map<LocalDate, Boolean>> completionMap = new HashMap<>();
        for (HabitCompletion hc : completions) {
            Long habitId = hc.getHabit().getId();
            completionMap
                    .computeIfAbsent(habitId, k -> new HashMap<>())
                    .put(hc.getCompletedDate(), hc.isCompleted());
        }

        List<LocalDate> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(weekStart.plusDays(i));
        }

        return habits.stream().map(h -> {
            Map<LocalDate, Boolean> byDate = new LinkedHashMap<>();
            Map<LocalDate, Boolean> existed = completionMap.getOrDefault(h.getId(), Map.of());

            for (LocalDate d : days) {
                byDate.put(d, existed.getOrDefault(d, false));
            }

            return new HabitWeekResponse(h.getId(), h.getTitle(), byDate);
        }).toList();
    }
}
