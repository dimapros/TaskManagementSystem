package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.enums.Priority;
import com.example.taskmanagement.model.enums.Status;
import com.example.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "API для управления заданиями")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получить все задания", description = "Возвращает список всех имеющихся заданий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задания успешно получены",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "204", description = "Задания отсутствуют"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
    })
    public ResponseEntity<Page<Task>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {

        Page<Task> tasks = taskService.getAllTasks(page, size);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    @Operation(summary = "Получить задание", description = "Возвращает одно задание по конкретному taskId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задание успешно получено",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задание не было найдено",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Optional<Task> optionalTask = taskService.getTask(id);

        return optionalTask.map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authorTasks")
    @Operation(summary = "Получить все задания конкретного автора",
            description = "Возвращает все задания по конкретному authorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задания успешно получены",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задания или автор не были найдены",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<Task>> getAuthorTask(@RequestParam Long authorId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) {

        Page<Task> foundTasks = taskService.getAuthorTask(page, size, authorId);

        return new ResponseEntity<>(foundTasks, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/executorTasks")
    @Operation(summary = "Получить все задания пользователя на исполнении",
            description = "Возвращает все задания по конкретному executorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задания успешно получены",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задания или исполнитель не были найдены",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Page<Task>> getExecutorTask(@RequestParam Long executorId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size) {

        Page<Task> foundTasks = taskService.getExecutorTask(page, size, executorId);

        return new ResponseEntity<>(foundTasks, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/newTask")
    @Operation(summary = "Создать новое задание",
            description = "Создание нового задания и прикрепление текущего автора к ней")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задание успешно создано",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> createNewTask(@Valid @RequestBody Task task, Principal principal) {
        Task createdTask = taskService.create(task, principal.getName());

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/changedStatusAndPriority")
    @Operation(summary = "Изменить статус и приоритет задачи", description = "Изменяет статус и приоритет задачи(только автор)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус и приоритет изменены",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задание не было найдено",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> changeStatusAndPriority(@RequestParam Long taskId,
                                                        @Valid @RequestParam Status status,
                                                        @Valid @RequestParam Priority priority) {

        Task updatedTaskEntity = taskService.changeTask(taskId, status, priority);
        return new ResponseEntity<>(updatedTaskEntity, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or @taskServiceImpl.isExecutor(#taskId, principal.username)")
    @PutMapping("/changedStatus")
    @Operation(summary = "Изменить статус задачи", description = "Изменяет статус задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус изменен",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задание не было найдено",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> changeStatus(@RequestParam Long taskId,
                                             @Valid @RequestParam Status status) {

        Task updatedTaskEntity = taskService.changeTask(taskId, status);
        return new ResponseEntity<>(updatedTaskEntity, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/executorTask")
    @Operation(summary = "Добавить исполнителя к задаче", description = "Добавляет исполнителя к задаче по taskId и executorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Исполнитель был успешно добавлен",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задание или пользователь не был найден",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Задание уже выполняется другим пользователем",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> addExecutorTask(@RequestParam Long taskId, @RequestParam Long executorId) {
        return new ResponseEntity<>(taskService.addExecutorTask(taskId, executorId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    @Operation(summary = "Удалить задание", description = "удаляет задание по конкретному taskId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задание было успешно удалено",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задание не было найдено",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> deleteTask(@RequestParam Long id) {
        taskService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
