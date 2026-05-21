package com.example.monolith_dpt.repository;

import com.example.monolith_dpt.entity.FocusSession;
import com.example.monolith_dpt.entity.FocusSessionStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {
    @Query("""
        select fs from FocusSession fs
        where fs.task.id = :taskId
          and fs.status = com.example.monolith_dpt.entity.FocusSessionStatus.RUNNING
          and fs.endedAt is null
        """)
    Optional<FocusSession> findRunningByTaskId(Long taskId);

    List<FocusSession> findByTask_IdOrderByStartedAtDesc(Long taskId);

    @Query("""
        select fs
        from FocusSession fs
        where fs.status = :status
          and fs.startedAt >= :from
          and fs.startedAt < :to
          and fs.endedAt is not null
    """)
    List<FocusSession> findFinishedSessionsInRange(
            @Param("status") FocusSessionStatus status,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select fs from FocusSession fs
    where fs.task.id = :taskId
      and fs.status = com.example.monolith_dpt.entity.FocusSessionStatus.RUNNING
      and fs.endedAt is null
    """)
    Optional<FocusSession> findRunningByTaskIdForUpdate(@Param("taskId") Long taskId);
}
