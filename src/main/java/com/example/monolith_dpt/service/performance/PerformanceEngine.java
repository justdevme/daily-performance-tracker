package com.example.monolith_dpt.service.performance;

import com.example.monolith_dpt.dto.PerformanceDailyResponse;

import com.example.monolith_dpt.entity.performance.ScoreMetric;
import com.example.monolith_dpt.service.performance.normalize.ScoreNormalizer;

import com.example.monolith_dpt.service.performance.normalize.weight.WeightResolver;
import com.example.monolith_dpt.service.performance.normalize.weight.WeightedAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class PerformanceEngine {

    private final TaskRawMetricCalculator taskRaw;
    private final FocusRawMetricCalculator focusRaw;
    private final HabitRawMetricCalculator habitRaw;

    private final ScoreNormalizer normalizer;
    private final WeightedAggregator aggregator;
    private final WeightResolver weightResolver;

    @Qualifier("performanceExecutor")
    private final Executor executor;

    public PerformanceDailyResponse calculate(LocalDate date) {

        CompletableFuture<Long> taskFuture = CompletableFuture.supplyAsync(
                () -> taskRaw.doneCount(date), executor
        );

        CompletableFuture<Long> focusFuture = CompletableFuture.supplyAsync(
                () -> focusRaw.totalMinutes(date), executor
        );

        CompletableFuture<HabitRawMetricCalculator.HabitRawResult> habitFuture = CompletableFuture.supplyAsync(
                () -> habitRaw.completionRate(date), executor
        );

        CompletableFuture.allOf(taskFuture, focusFuture, habitFuture).join();

        long doneTasks    = taskFuture.join();
        long focusMinutes = focusFuture.join();
        var habitRawResult   = habitFuture.join();
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