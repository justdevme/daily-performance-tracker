package com.example.monolith_dpt.service.performance;

import com.example.monolith_dpt.entity.FocusSession;
import com.example.monolith_dpt.entity.FocusSessionStatus;
import com.example.monolith_dpt.repository.FocusSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FocusRawMetricCalculator {

    private final FocusSessionRepository focusRepo;

    public long totalMinutes(LocalDate date) {
        Instant from = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<FocusSession> sessions =
                focusRepo.findFinishedSessionsInRange(FocusSessionStatus.COMPLETED, from, to);

        return sessions.stream()
                .mapToLong(s -> Duration.between(s.getStartedAt(), s.getEndedAt()).toMinutes())
                .filter(m -> m > 0)
                .sum();
    }
}