package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.entity.Role;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.service.RoleService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Tag(name = "Role Controller", description = "API для управления ролями")
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/role")
    @Operation(summary = "Добавить роли", description = "Добавление новых ролей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно добавлена",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Роль уже существует",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Role> addRole(@Valid @RequestBody Role role) {

        return new ResponseEntity<>(roleService.addRole(role), HttpStatus.CREATED);
    }
}
