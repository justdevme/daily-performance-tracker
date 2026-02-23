package com.example.monolith_dpt.repository;

import com.example.monolith_dpt.entity.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
