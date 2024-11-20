package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAuthorId(Long authorId, Pageable pageable);

    Page<Task> findByExecutorId(Long executorId, Pageable pageable);
}
