package com.example.monolith_dpt.service.performance.normalize.weight;


import com.example.monolith_dpt.entity.performance.ScoreMetric;
import com.example.monolith_dpt.repository.performance.ScoreWeightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WeightResolver {

    private final ScoreWeightRepository repo;

    public BigDecimal getGlobalWeight(ScoreMetric metric) {
        return repo.findActiveGlobal(metric)
                .map(w -> w.getWeight())
                .orElseGet(() -> fallback(metric));
    }

    private BigDecimal fallback(ScoreMetric metric) {
        return switch (metric) {
            case TASK -> BigDecimal.valueOf(0.4);
            case HABIT -> BigDecimal.valueOf(0.3);
            case FOCUS -> BigDecimal.valueOf(0.3);
        };
    }
}