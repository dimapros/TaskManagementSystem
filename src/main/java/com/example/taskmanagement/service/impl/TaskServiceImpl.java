package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.exception.TaskAlreadyHasExecutorException;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.model.enums.Priority;
import com.example.taskmanagement.model.enums.Status;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Task> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskRepository.findAll(pageable);

        if (taskPage.isEmpty()) {
            throw new EntityNotFoundException("Нет ни одной задачи");
        }
        return taskPage;
    }

    @Override
    public Optional<Task> getTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            return taskRepository.findById(id);
        }
        throw new EntityNotFoundException("Задания не существует");
    }

    @Override
    public Page<Task> getAuthorTask(int page, int size, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Такого пользователя не существует");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> listTask = taskRepository.findByAuthorId(id, pageable);

        if (listTask.isEmpty()) {
            throw new EntityNotFoundException("Данный автор не создал ни одной задачи");
        }
        return listTask;
    }

    @Override
    public Page<Task> getExecutorTask(int page, int size, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Такого пользователя не существует");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> listTask = taskRepository.findByExecutorId(id, pageable);

        if (listTask.isEmpty()) {
            throw new EntityNotFoundException("Данный пользователь не выполняет ни одной задачи");
        }
        return listTask;
    }

    @Override
    public Task create(Task task, String email) {
        User author = userRepository.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("User not found with email " + email));

        task.setAuthor(author);

        return taskRepository.save(task);
    }

    @Override
    public Task changeTask(Long id, Status status, Priority priority) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task changedTask = task.get();
            changedTask.setTaskStatus(status);
            changedTask.setTaskPriority(priority);

            return taskRepository.save(changedTask);
        } else {
            throw new EntityNotFoundException("Задания по такому id не существует");
        }
    }

    @Override
    public Task changeTask(Long id, Status status) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task changedTask = task.get();
            changedTask.setTaskStatus(status);

            return taskRepository.save(changedTask);
        } else {
            throw new EntityNotFoundException("Задания по такому id не существует");
        }
    }

    @Override
    public Task addExecutorTask(Long taskId, Long executorId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задания по такому id не существует"));

        User user = userRepository.findById(executorId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя по такому id не существует"));

        if (task.getExecutor() != null) {
            throw new TaskAlreadyHasExecutorException("Это задание уже имеет исполнителя");
        }

        task.setExecutor(user);
        return taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Задания по такому id не существует");
        }
    }

    @Override
    public boolean isExecutor(Long taskId, String email) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            User executor = task.getExecutor();
            return executor != null && executor.getEmail().equals(email);
        }
        return false;
    }
}
