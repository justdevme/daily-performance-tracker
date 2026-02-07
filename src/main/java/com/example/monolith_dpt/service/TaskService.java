package com.example.monolith_dpt.service;

import com.example.monolith_dpt.dto.CreateTaskRequest;
import com.example.monolith_dpt.entity.*;
import com.example.monolith_dpt.repository.ReflectionRepository;
import com.example.monolith_dpt.repository.TaskRepository;
import com.example.monolith_dpt.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final WeekRepository weekRepository;
    private ReflectionRepository reflectionRepository;

    public Task createTask(CreateTaskRequest req) {
        Week week = weekRepository.findById(req.weekId())
                .orElseThrow(() -> new RuntimeException("Week not found: " + req.weekId()));

        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .status(req.status() == null ? TaskStatus.PENDING : req.status())
                .priority(req.priority() == null ? Priority.MEDIUM : req.priority())
                .dayOfWeek(req.dayOfWeek())
                .dueDate(req.dueDate())
                .week(week)
                .build();

        return taskRepository.save(task);
    }

    public Task get(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // Get all tasks for a specific week
    public List<Task> getTasksForWeek(int weekNumber) {
        Week week = weekRepository.findByWeekNumber(weekNumber).orElseThrow(() -> new RuntimeException("Week not found"));
        return taskRepository.findByWeekId(week.getId());
    }

    // Get reflections for a specific week
    public List<Reflection> getReflectionsForWeek(int weekNumber) {
        Week week = weekRepository.findByWeekNumber(weekNumber).orElseThrow(() -> new RuntimeException("Week not found"));
        return reflectionRepository.findByWeekId(week.getId());
    }

    // Update task
    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        return taskRepository.save(task);
    }

    // Delete task
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
