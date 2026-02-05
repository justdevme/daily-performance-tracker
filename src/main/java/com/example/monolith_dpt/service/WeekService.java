package com.example.monolith_dpt.service;

import com.example.monolith_dpt.entity.Week;
import com.example.monolith_dpt.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;

    public List<Week> seedMonth(int month, int year) {
        LocalDate first = LocalDate.of(year, month, 1);
        LocalDate last = first. withDayOfMonth(first.lengthOfMonth());

        List<Week> weeks = new ArrayList<>();

        weeks.add(makeWeek(1, month, year, first, min(last, first.plusDays(6))));
        weeks.add(makeWeek(2, month, year, first.plusDays(7), min(last, first.plusDays(13))));
        weeks.add(makeWeek(3, month, year, first.plusDays(14), min(last, first.plusDays(20))));
        weeks.add(makeWeek(4, month, year, first.plusDays(21), last));

        for (Week w : weeks) {
            if (!weekRepository.existsByMonthAndYearAndWeekNumber(month, year, w.getWeekNumber())) {
                weekRepository.save(w);
            }
        }
        return weekRepository.findByMonthAndYearOrderByWeekNumberAsc(month, year);
    }

    private Week makeWeek(int weekNumber, int month, int year, LocalDate start, LocalDate end) {
        return Week.builder()
                .weekNumber(weekNumber)
                .month(month)
                .year(year)
                .startDate(start)
                .endDate(end)
                .build();
    }

    private LocalDate min(LocalDate a, LocalDate b) {
        return a.isBefore(b) ? a : b;
    }

    public List<Week> list(int month, int year) {
        return weekRepository.findByMonthAndYearOrderByWeekNumberAsc(month, year);
    }
}
