package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.exception.RoleAlreadyExistsException;
import com.example.taskmanagement.model.entity.Role;
import com.example.taskmanagement.repository.RoleRepository;
import com.example.taskmanagement.util.RoleUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    @DisplayName("добавление роли, позитивный сценарий")
    void addRolePositiveTest() {
        Role role = RoleUtil.createUserRole();
        String name = role.getName();

        when(roleRepository.findByName(name)).thenReturn(Optional.empty());
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.addRole(role);

        assertEquals(role, result);
        verify(roleRepository, times(1)).findByName(name);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    @DisplayName("добавление роли, негативный сценарий")
    void addRoleNegativeTest() {
        Role role = RoleUtil.createUserRole();
        String name = role.getName();

        when(roleRepository.findByName(name)).thenReturn(Optional.of(role));

        assertThrows(RoleAlreadyExistsException.class, () -> roleService.addRole(role));

        verify(roleRepository, times(1)).findByName(name);
        verify(roleRepository, never()).save(any(Role.class));
    }
}