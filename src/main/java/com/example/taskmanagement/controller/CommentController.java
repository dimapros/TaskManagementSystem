package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.entity.Comment;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Tag(name = "Comment Controller", description = "API для управления комментариями")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAuthority('ADMIN') or @taskServiceImpl.isExecutor(#taskId, principal.username)")
    @PostMapping("/comment")
    @Operation(summary = "Добавить комментарии", description = "Добавляет комментарии к конкретной задаче по taskId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задача или пользователь не былы найдены",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Comment> addComment(@Valid @RequestBody Comment comment,
                                              @RequestParam Long taskId,
                                              Principal principal) {
        return new ResponseEntity<>(commentService.addComment(comment, taskId, principal.getName()), HttpStatus.CREATED);
    }
}
