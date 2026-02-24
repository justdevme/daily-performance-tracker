package com.example.monolith_dpt.repository.performance;

import com.example.monolith_dpt.entity.performance.ScoreMetric;
import com.example.monolith_dpt.entity.performance.ScoreWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ScoreWeightRepository extends JpaRepository<ScoreWeight, UUID> {
    @Query("""
        SELECT w FROM ScoreWeight w
        WHERE w.scope = 'GLOBAL'
          AND w.metric = :metric
          AND w.isActive = true
    """)
    Optional<ScoreWeight> findActiveGlobal(@Param("metric") ScoreMetric metric);
}
