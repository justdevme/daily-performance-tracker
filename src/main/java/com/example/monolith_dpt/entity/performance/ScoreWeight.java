package com.example.monolith_dpt.entity.performance;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "score_weight",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_score_weight_scope_metric", columnNames = {"scope", "metric"})
        },
        indexes = {
                @Index(name = "idx_score_weight_active", columnList = "is_active")
        }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ScoreWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScoreScope scope;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ScoreMetric metric;

    @Column(nullable = false, precision = 6, scale = 4)
    private java.math.BigDecimal weight; // ví dụ 0.4000

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

}