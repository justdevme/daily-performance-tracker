package com.example.monolith_dpt.service;

import com.example.monolith_dpt.dto.FocusSessionResponse;
import com.example.monolith_dpt.dto.StartFocusSessionRequest;
import com.example.monolith_dpt.dto.StopFocusSessionRequest;
import com.example.monolith_dpt.entity.FocusSession;
import com.example.monolith_dpt.entity.FocusSessionStatus;
import com.example.monolith_dpt.entity.Task;
import com.example.monolith_dpt.repository.FocusSessionRepository;
import com.example.monolith_dpt.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FocusSessionService {

    private final TaskRepository taskRepository;
    private final FocusSessionRepository focusSessionRepository;

    public FocusSessionResponse startSession(Long taskId, StartFocusSessionRequest req) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        focusSessionRepository.findRunningByTaskId(taskId).ifPresent(running -> {
            throw new IllegalStateException("Task already has a running focus session" + running.getId());
        });

        Instant startedAt = (req != null && req.startedAt() != null) ? req.startedAt() : Instant.now();

        FocusSession session = FocusSession.builder()
                .task(task)
                .status(FocusSessionStatus.RUNNING)
                .startedAt(startedAt)
                .endedAt(null)
                .build();

        FocusSession saved = focusSessionRepository.save(session);
        return toResponse(saved, Instant.now());
    }

     public FocusSessionResponse stopSession(Long taskId, Long sessionId, StopFocusSessionRequest req) {
         FocusSession session = focusSessionRepository.findById(sessionId)
                 .orElseThrow(() -> new IllegalArgumentException("FocusSession not found: " + sessionId));

         if (!session.getTask().getId().equals(taskId)) {
             throw new IllegalStateException("Session does not belong to this task.");
         }
         if (!session.isRunning()) {
             throw new IllegalStateException("Session is not running.");
         }

         Instant endedAt = (req != null && req.endedAt() != null) ? req.endedAt() : Instant.now();
         if (endedAt.isBefore(session.getStartedAt())) {
             throw new IllegalArgumentException("endedAt must be >= startedAt");
         }

         String action = (req != null && req.action() != null) ? req.action() : "COMPLETE";
         if ("CANCEL".equalsIgnoreCase(action)) {
             session.setStatus(FocusSessionStatus.CANCELED);
         } else {
             session.setStatus(FocusSessionStatus.COMPLETED);
         }
         session.setEndedAt(endedAt);

         FocusSession saved = focusSessionRepository.save(session);
         return toResponse(saved, Instant.now());
     }
    @Transactional(readOnly = true)
    public List<FocusSessionResponse> listSessions(Long taskId) {
        return focusSessionRepository.findByTask_IdOrderByStartedAtDesc(taskId).stream()
                .map(fs -> toResponse(fs, Instant.now()))
                .toList();
    }

    @Transactional(readOnly = true)
    public FocusSessionResponse getRunningSession(Long taskId) {
        return focusSessionRepository.findRunningByTaskId(taskId)
                .map(fs -> toResponse(fs, Instant.now()))
                .orElse(null);
    }
    private FocusSessionResponse toResponse(FocusSession fs, Instant now) {
        Instant end = fs.getEndedAt();
        long seconds;
        if (end != null) seconds = Duration.between(fs.getStartedAt(), end).getSeconds();
        else seconds = Duration.between(fs.getStartedAt(), now).getSeconds();

        return new FocusSessionResponse(
                fs.getId(),
                fs.getTask().getId(),
                fs.getStatus(),
                fs.getStartedAt(),
                fs.getEndedAt(),
                Math.max(seconds, 0)
        );
    }
}
