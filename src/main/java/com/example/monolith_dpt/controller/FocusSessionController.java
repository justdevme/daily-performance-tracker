package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.dto.FocusSessionResponse;
import com.example.monolith_dpt.dto.StartFocusSessionRequest;
import com.example.monolith_dpt.dto.StopFocusSessionRequest;
import com.example.monolith_dpt.service.FocusSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/{taskId}/focus-sessions")
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    @PostMapping("/start")
    public FocusSessionResponse start(@PathVariable Long taskId,
                                      @RequestBody(required = false) StartFocusSessionRequest req) {
        return focusSessionService.startSession(taskId, req);
    }

    @PostMapping("/{sessionId}/stop")
    public FocusSessionResponse stop(@PathVariable Long taskId,
                                     @PathVariable Long sessionId,
                                     @RequestBody(required = false) StopFocusSessionRequest req) {
        return focusSessionService.stopSession(taskId, sessionId, req);
    }

    @GetMapping
    public List<FocusSessionResponse> list(@PathVariable Long taskId) {
        return focusSessionService.listSessions(taskId);
    }

    @GetMapping("/running")
    public FocusSessionResponse running(@PathVariable Long taskId) {
        return focusSessionService.getRunningSession(taskId);
    }
}
