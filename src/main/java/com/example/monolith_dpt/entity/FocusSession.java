package com.example.monolith_dpt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

// id, task, status, startedAt, endedAt
@Getter
@Setter
@Entity
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FocusSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FocusSessionStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    public boolean isRunning() {
        return this.status == FocusSessionStatus.RUNNING && this.endedAt == null;
    }

}
