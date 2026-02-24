package com.example.monolith_dpt.service.performance;

import com.example.monolith_dpt.dto.PerformanceDailyResponse;

import com.example.monolith_dpt.entity.performance.ScoreMetric;
import com.example.monolith_dpt.service.performance.normalize.ScoreNormalizer;

import com.example.monolith_dpt.service.performance.normalize.weight.WeightResolver;
import com.example.monolith_dpt.service.performance.normalize.weight.WeightedAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PerformanceEngine {

    private final TaskRawMetricCalculator taskRaw;
    private final FocusRawMetricCalculator focusRaw;
    private final HabitRawMetricCalculator habitRaw;

    private final ScoreNormalizer normalizer;
    private final WeightedAggregator aggregator;
    private final WeightResolver weightResolver;

    public PerformanceDailyResponse calculate(LocalDate date) {

        long doneTasks = taskRaw.doneCount(date);
        long focusMinutes = focusRaw.totalMinutes(date);
        var habitRawResult = habitRaw.completionRate(date);

        // NORMALIZE (0..100) -> BigDecimal
        BigDecimal taskScore = normalizer.taskScore(doneTasks);
        BigDecimal focusScore = normalizer.focusScore(focusMinutes);
        BigDecimal habitScore = normalizer.habitScore(habitRawResult.completionRate());

        Map<ScoreMetric, BigDecimal> metricScores = Map.of(
                ScoreMetric.TASK, taskScore,
                ScoreMetric.HABIT, habitScore,
                ScoreMetric.FOCUS, focusScore
        );

        BigDecimal finalScore = aggregator.aggregate(metricScores);

        Map<String, BigDecimal> weights = Map.of(
                "TASK", weightResolver.getGlobalWeight(ScoreMetric.TASK),
                "HABIT", weightResolver.getGlobalWeight(ScoreMetric.HABIT),
                "FOCUS", weightResolver.getGlobalWeight(ScoreMetric.FOCUS)
        );

        return PerformanceDailyResponse.builder()
                .date(date)
                .doneTasks(doneTasks)
                .focusMinutes(focusMinutes)
                .totalHabits(habitRawResult.totalHabits())
                .completedHabits(habitRawResult.completedHabits())
                .habitCompletionRate(habitRawResult.completionRate())

                // nếu DTO đang là double thì đổi DTO sang BigDecimal (khuyên),
                // hoặc tạm thời .doubleValue()
                .taskScore(taskScore.doubleValue())
                .habitScore(habitScore.doubleValue())
                .focusScore(focusScore.doubleValue())
                .weights(weights.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().doubleValue()
                        )))
                .finalScore(finalScore.doubleValue())
                .build();
    }
}