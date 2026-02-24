package com.example.monolith_dpt.entity.performance;

import com.example.monolith_dpt.entity.Habit;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "habit_completion",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_habit_date", columnNames = {"habit_id", "completed_date"})
        }
)
public class HabitCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "completed_date", nullable = false)
    private LocalDate completedDate;

    @Column(nullable = false)
    private boolean completed;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}