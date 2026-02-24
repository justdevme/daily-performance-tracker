package com.example.monolith_dpt.service.performance.normalize;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ScoreNormalizer {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private static final long TASK_TARGET_DONE_PER_DAY = 5;
    private static final long FOCUS_TARGET_MINUTES_PER_DAY = 120;

    public BigDecimal taskScore(long doneCount) {
        BigDecimal ratio = BigDecimal.valueOf(doneCount)
                .divide(BigDecimal.valueOf(TASK_TARGET_DONE_PER_DAY), 6, RoundingMode.HALF_UP);

        return capTo100(ratio.multiply(HUNDRED));
    }

    // completionRate: 0..1
    public BigDecimal habitScore(double completionRate) {
        BigDecimal rate = BigDecimal.valueOf(completionRate);
        return capTo100(rate.multiply(HUNDRED));
    }

    public BigDecimal focusScore(long minutes) {
        BigDecimal ratio = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(FOCUS_TARGET_MINUTES_PER_DAY), 6, RoundingMode.HALF_UP);

        return capTo100(ratio.multiply(HUNDRED));
    }

    private BigDecimal capTo100(BigDecimal value) {
        if (value.compareTo(ZERO) < 0) return ZERO;
        if (value.compareTo(HUNDRED) > 0) return HUNDRED;
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}