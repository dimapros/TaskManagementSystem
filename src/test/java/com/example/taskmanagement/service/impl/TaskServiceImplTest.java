package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.exception.TaskAlreadyHasExecutorException;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.enums.Priority;
import com.example.taskmanagement.model.enums.Status;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.util.TaskUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("поиск всех заданий, позитивный сценарий")
    void findAllTasksPositiveTest() {
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        List<Task> tasks = Arrays.asList(TaskUtil.getTask1(), TaskUtil.getTask2());
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        Page<Task> result = taskService.getAllTasks(page, size);

        assertEquals(2, result.getTotalElements());
        assertEquals(TaskUtil.getTask1(), result.getContent().get(0));
        assertEquals(TaskUtil.getTask2(), result.getContent().get(1));
    }

    @Test
    @DisplayName("поиск всех заданий, негативный сценарий")
    public void findAllTasksNegativeTest() {
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(taskRepository.findAll(pageable)).thenReturn(emptyPage);

        assertThrows(EntityNotFoundException.class, () -> taskService.getAllTasks(page, size));

        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("поиск задания по id, позитивный сценарий")
    public void findTaskByIdPositiveTest() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.of(TaskUtil.getTaskWithoutExecutor()));

        Optional<Task> result = taskService.getTask(id);

        assertTrue(result.isPresent());
        assertEquals(TaskUtil.getTaskWithoutExecutor(), result.get());
        assertEquals("Валидация", result.get().getTitle());
        assertEquals("Добавить валидацию", result.get().getDescription());
        assertEquals(Status.WAITING, result.get().getTaskStatus());
        assertEquals(Priority.MEDIUM, result.get().getTaskPriority());
        assertEquals(TaskUtil.getAuthor(), result.get().getAuthor());
    }

    @Test
    @DisplayName("поиск задания по id, негативный сценарий")
    public void findTaskByIdNegativeTest() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTask(id));

        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("поиск задания по автору, позитивный сценарий")
    public void findTaskByAuthorIdPositiveTest() {
        Long id = 1L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        List<Task> tasks = Arrays.asList(TaskUtil.getTask1(), TaskUtil.getTask2());
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(userRepository.findById(id)).thenReturn(Optional.of(TaskUtil.getAuthor()));
        when(taskRepository.findByAuthorId(id, pageable)).thenReturn(taskPage);

        Page<Task> result = taskService.getAuthorTask(page, size, id);

        assertEquals(2, result.getTotalElements());
        assertEquals(TaskUtil.getTask1(), result.getContent().get(0));
        assertEquals(TaskUtil.getTask2(), result.getContent().get(1));
    }

    @Test
    @DisplayName("поиск задания по автору, негативный сценарий")
    public void findTaskByAuthorIdNegativeTest() {
        Long id = 1L;
        int page = 0;
        int size = 5;

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(userRepository.findById(id)).thenReturn(Optional.of(TaskUtil.getAuthor()));
        when(taskRepository.findByAuthorId(id, pageable)).thenReturn(emptyPage);

        assertThrows(EntityNotFoundException.class, () -> taskService.getAuthorTask(page, size, id));

        verify(taskRepository, times(1)).findByAuthorId(id, pageable);
    }

    @Test
    @DisplayName("поиск задания по исполнителю, позитивный сценарий")
    public void findTaskByExecutorIdPositiveTest() {
        Long id = 1L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        List<Task> tasks = Arrays.asList(TaskUtil.getTask1(), TaskUtil.getTask2());
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(userRepository.findById(id)).thenReturn(Optional.of(TaskUtil.getExecutor()));
        when(taskRepository.findByExecutorId(id, pageable)).thenReturn(taskPage);

        Page<Task> result = taskService.getExecutorTask(page, size, id);

        assertEquals(2, result.getTotalElements());
        assertEquals(TaskUtil.getTask1(), result.getContent().get(0));
        assertEquals(TaskUtil.getTask2(), result.getContent().get(1));
    }

    @Test
    @DisplayName("поиск задания по исполнителю, негативный сценарий")
    public void findTaskByExecutorIdNegativeTest() {
        Long id = 1L;
        int page = 0;
        int size = 5;

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(userRepository.findById(id)).thenReturn(Optional.of(TaskUtil.getAuthor()));
        when(taskRepository.findByExecutorId(id, pageable)).thenReturn(emptyPage);

        assertThrows(EntityNotFoundException.class, () -> taskService.getExecutorTask(page, size, id));

        verify(taskRepository, times(1)).findByExecutorId(id, pageable);
    }

    @Test
    @DisplayName("создание задачи, позитивный сценарий")
    public void saveTaskPositiveTest() {
        Task task = TaskUtil.getTask1();

        when(userRepository.findByEmail("author@mail.ru")).thenReturn(Optional.of(TaskUtil.getAuthor()));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.create(task, "author@mail.ru");

        assertEquals(task, result);
    }

    @Test
    @DisplayName("создание задачи, негативный сценарий")
    public void saveTaskNegativeTest() {
        Task task = TaskUtil.getTaskWithoutAuthor();

        when(userRepository.findByEmail("alexandr@mail.ru")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.create(task, "alexandr@mail.ru"));

        verify(userRepository, times(1)).findByEmail("alexandr@mail.ru");
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("изменение статуса и приоритета, позитивный сценарий")
    public void changeStatusAndPriorityPositiveTest() {
        Long id = 1L;

        Task beforeChange = TaskUtil.getTask1();
        Task afterChange = TaskUtil.getTaskChangedStatusPriority();

        when(taskRepository.findById(id)).thenReturn(Optional.of(beforeChange));
        when(taskRepository.save(afterChange)).thenReturn(afterChange);

        Task result = taskService.changeTask(1L, Status.RUNNING, Priority.MEDIUM);

        assertEquals(afterChange, result);
    }

    @Test
    @DisplayName("изменение статуса и приоритета, негативный сценарий")
    public void changeStatusAndPriorityNegativeTest() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.changeTask(id, Status.RUNNING, Priority.MEDIUM));

        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("изменение статуса, позитивный сценарий")
    public void changeStatusPositiveTest() {
        Long id = 1L;

        Task beforeChange = TaskUtil.getTask1();
        Task afterChange = TaskUtil.getTaskChangedStatus();

        when(taskRepository.findById(id)).thenReturn(Optional.of(beforeChange));
        when(taskRepository.save(afterChange)).thenReturn(afterChange);

        Task result = taskService.changeTask(1L, Status.RUNNING);

        assertEquals(afterChange, result);
    }

    @Test
    @DisplayName("изменение статуса, негативный сценарий")
    public void changeStatusNegativeTest() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.changeTask(id, Status.RUNNING));

        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("добавление исполнителя к задаче, позитивный сценарий")
    public void addExecutorTaskPositiveTest() {
        Long userId = 1L;
        Long taskId = 1L;

        Task beforeChange = TaskUtil.getTaskWithoutExecutor();
        Task afterChange = TaskUtil.getTaskWithExecutor();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(beforeChange));
        when(userRepository.findById(userId)).thenReturn(Optional.of(TaskUtil.getExecutor()));
        when(taskRepository.save(afterChange)).thenReturn(afterChange);

        Task result = taskService.addExecutorTask(1L, 1L);

        assertEquals(afterChange, result);
    }

    @Test
    @DisplayName("добавление исполнителя к задаче, негативный сценарий: задача не найдена")
    public void addExecutorTaskNegativeTestTaskNotFound() {
        Long taskId = 1L;
        Long userId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.addExecutorTask(taskId, userId));

        verify(taskRepository, times(1)).findById(taskId);
        verify(userRepository, never()).findById((userId));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("добавление исполнителя к задаче, негативный сценарий: пользователь не найден")
    public void addExecutorTaskNegativeTestUserNotFound() {
        Long taskId = 1L;
        Long userId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(TaskUtil.getTaskWithoutExecutor()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.addExecutorTask(taskId, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("добавление исполнителя к задаче, негативный сценарий: задача уже имеет исполнителя")
    public void addExecutorTaskNegativeTestTaskAlreadyHasExecutor() {
        Long taskId = 1L;
        Long userId = 1L;

        Task beforeChange = TaskUtil.getTaskWithExecutor();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(beforeChange));
        when(userRepository.findById(userId)).thenReturn(Optional.of(TaskUtil.getExecutor()));

        assertThrows(TaskAlreadyHasExecutorException.class, () -> taskService.addExecutorTask(taskId, userId));
    }

    @Test
    @DisplayName("удаление задачи, позитивный сценарий")
    public void deleteTaskPositiveTest() {
        Long id = 1L;

        Task task = TaskUtil.getTask1();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(id);

        assertDoesNotThrow(() -> taskService.delete(id));

        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("удаление задачи, негативный сценарий")
    public void deleteTaskNegativeTest() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.delete(id));

        verify(taskRepository, never()).delete(any(Task.class));
    }

    @Test
    @DisplayName("является ли user исполнителем задачи, позитивный сценарий")
    public void isExecutorTaskPositiveTest() {
        Long id = 1L;
        String email = "executor@mail.ru";

        Task task = TaskUtil.getTask1();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        boolean result = taskService.isExecutor(id, email);

        assertTrue(result);
    }

    @Test
    @DisplayName("является ли user исполнителем задачи, негативный сценарий")
    public void isExecutorTaskNegativeTest() {
        Long taskId = 1L;
        String email = "sasha.ru.98@mail.ru";

        Task task = TaskUtil.getTask1();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        boolean result = taskService.isExecutor(taskId, email);

        verify(taskRepository, times(1)).findById(taskId);

        assertFalse(result);
    }
}