package com.example.monolith_dpt.service.performance;

import com.example.monolith_dpt.entity.TaskStatus;
import com.example.monolith_dpt.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskRawMetricCalculator {

    private final TaskRepository taskRepository;

    public long doneCount(LocalDate date) {
        return taskRepository.countByStatusAndDueDate(TaskStatus.COMPLETED, date);
    }
}