package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.service.UserService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Validated
@Tag(name = "User Controller", description = "API для управления пользователями")
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    @Operation(summary = "Зарегистрировать пользователя", description = "Регистрирует пользователя в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        try {
            userService.getUserByEmail(user.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (EntityNotFoundException e) {
            User createdUser = userService.addUser(user);

            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/newRole")
    @Operation(summary = "Установить роль пользователю", description = "Установление роли конкретному пользователю по userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно установлена",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задание или роль не были найдены",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<User> addRole(@RequestParam Long roleId, @RequestParam Long userId) {
        User user = userService.setRoleToUser(roleId, userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
