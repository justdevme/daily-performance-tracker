package com.example.monolith_dpt.controller;

import com.example.monolith_dpt.dto.CreateTaskRequest;
import com.example.monolith_dpt.entity.Reflection;
import com.example.monolith_dpt.entity.Task;
import com.example.monolith_dpt.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public Task createTask (@RequestBody CreateTaskRequest req) {
        return taskService.createTask(req);
    }

    /*@GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return taskService.get(id);
    }*/

    @GetMapping
    public List<Task> getByWeek(@RequestParam int weekNumber) {
        return taskService.getTasksForWeek(weekNumber);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

}
