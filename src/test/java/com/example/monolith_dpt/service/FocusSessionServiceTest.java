package com.example.monolith_dpt.service;

import com.example.monolith_dpt.dto.FocusSessionResponse;
import com.example.monolith_dpt.entity.FocusSession;
import com.example.monolith_dpt.entity.FocusSessionStatus;
import com.example.monolith_dpt.entity.Task;
import com.example.monolith_dpt.repository.FocusSessionRepository;
import com.example.monolith_dpt.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FocusSessionServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private FocusSessionRepository focusSessionRepository;

    @InjectMocks
    private FocusSessionService focusSessionService;

    // -------------------------------------------------------
    // Case 1: task tồn tại, không có running session → thành công
    // -------------------------------------------------------
    @Test
    void startSession_success() {
        // ARRANGE
        Long taskId = 1L;
        Task mockTask = Task.builder()
                .id(taskId)
                .title("Test task")
                .build();

        FocusSession savedSession = FocusSession.builder()
                .id(10L)
                .task(mockTask)
                .status(FocusSessionStatus.RUNNING)
                .startedAt(Instant.now())
                .build();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(mockTask));
        when(focusSessionRepository.findRunningByTaskIdForUpdate(taskId))
                .thenReturn(Optional.empty());
        when(focusSessionRepository.save(any(FocusSession.class)))
                .thenReturn(savedSession);

        // ACT
        FocusSessionResponse response = focusSessionService.startSession(taskId, null);

        // ASSERT
        assertThat(response.status()).isEqualTo(FocusSessionStatus.RUNNING);
        assertThat(response.taskId()).isEqualTo(taskId);
    }

    // -------------------------------------------------------
    // Case 2: đã có running session → throw IllegalStateException
    // -------------------------------------------------------
    @Test
    void startSession_alreadyRunning_throwsException() {
        // ARRANGE
        Long taskId = 1L;
        Task mockTask = Task.builder()
                .id(taskId)
                .title("Test task")
                .build();

        FocusSession runningSession = FocusSession.builder()
                .id(99L)
                .task(mockTask)
                .status(FocusSessionStatus.RUNNING)
                .startedAt(Instant.now())
                .build();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(mockTask));
        when(focusSessionRepository.findRunningByTaskIdForUpdate(taskId))
                .thenReturn(Optional.of(runningSession));

        // ACT & ASSERT
        assertThrows(
                IllegalStateException.class,
                () -> focusSessionService.startSession(taskId, null)
        );
    }

    // -------------------------------------------------------
    // Case 3: task không tồn tại → throw IllegalArgumentException
    // -------------------------------------------------------
    @Test
    void startSession_taskNotFound_throwsException() {
        // ARRANGE
        Long taskId = 999L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> focusSessionService.startSession(taskId, null)
        );
    }

    // -------------------------------------------------------
    // Case 4: stop session thành công → status COMPLETED
    // -------------------------------------------------------
    @Test
    void stopSession_success_completed() {
        // ARRANGE
        Long taskId = 1L;
        Long sessionId = 10L;

        Task mockTask = Task.builder()
                .id(taskId)
                .title("Test task")
                .build();

        FocusSession runningSession = FocusSession.builder()
                .id(sessionId)
                .task(mockTask)
                .status(FocusSessionStatus.RUNNING)
                .startedAt(Instant.now().minusSeconds(60))
                .build();

        FocusSession completedSession = FocusSession.builder()
                .id(sessionId)
                .task(mockTask)
                .status(FocusSessionStatus.COMPLETED)
                .startedAt(runningSession.getStartedAt())
                .endedAt(Instant.now())
                .build();

        when(focusSessionRepository.findById(sessionId))
                .thenReturn(Optional.of(runningSession));
        when(focusSessionRepository.save(any(FocusSession.class)))
                .thenReturn(completedSession);

        // ACT
        FocusSessionResponse response = focusSessionService.stopSession(taskId, sessionId, null);

        // ASSERT
        assertThat(response.status()).isEqualTo(FocusSessionStatus.COMPLETED);
        assertThat(response.durationSeconds()).isGreaterThan(0);
    }
}