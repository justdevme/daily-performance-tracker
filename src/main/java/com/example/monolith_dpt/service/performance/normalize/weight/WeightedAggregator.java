package com.example.monolith_dpt.service.performance.normalize.weight;

import com.example.monolith_dpt.entity.performance.ScoreMetric;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WeightedAggregator {

    private final WeightResolver resolver;

    public BigDecimal aggregate(Map<ScoreMetric, BigDecimal> metricScores) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal weightSum = BigDecimal.ZERO;

        for (var entry : metricScores.entrySet()) {
            BigDecimal w = resolver.getGlobalWeight(entry.getKey()); // BigDecimal
            BigDecimal score = entry.getValue() == null ? BigDecimal.ZERO : entry.getValue();

            // sum += score * w
            sum = sum.add(score.multiply(w));
            weightSum = weightSum.add(w);
        }

        if (weightSum.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        // normalize weights: sum / weightSum
        return sum.divide(weightSum, 6, RoundingMode.HALF_UP);
    }
}