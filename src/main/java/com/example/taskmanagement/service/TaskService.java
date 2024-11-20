package com.example.taskmanagement.service;

import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.enums.Priority;
import com.example.taskmanagement.model.enums.Status;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TaskService {
    Page<Task> getAllTasks(int page, int size);

    Task create(Task task, String email);

    Optional<Task> getTask(Long id);

    void delete(Long id);

    Task changeTask(Long id, Status status, Priority priority);

    Task changeTask(Long id, Status status);

    Task addExecutorTask(Long taskId, Long executorId);


    Page<Task> getAuthorTask(int page, int size, Long id);

    Page<Task> getExecutorTask(int page, int size, Long Id);

    boolean isExecutor(Long taskId, String username);
}
