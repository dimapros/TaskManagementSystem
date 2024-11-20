package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.exception.UserAlreadyExistsException;
import com.example.taskmanagement.model.entity.Role;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.repository.RoleRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.util.RoleUtil;
import com.example.taskmanagement.util.UserUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("добавление пользователя, позитивный сценарий")
    void addUserPositiveTest() {
        Long roleId = 2L;

        Role role = RoleUtil.createUserRole();
        User userBefore = UserUtil.getUserWithoutRoles();
        User userAfter = UserUtil.getUserAfterChangePassword();

        String expectedPassword = userBefore.getPassword();

        when(userRepository.findByEmail(userBefore.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(userBefore)).thenReturn(userAfter);
        when(passwordEncoder.encode(expectedPassword)).thenReturn("hashedPassword");

        User result = userService.addUser(userBefore);

        assertEquals(userAfter, result);
        assertEquals("hashedPassword", result.getPassword());

        verify(userRepository, times(1)).findByEmail(userBefore.getEmail());
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(expectedPassword);
    }

    @Test
    @DisplayName("добавление пользователя, негативный сценарий")
    void addUserNegativeTest() {
        User userBefore = UserUtil.getUserWithRoleUser();

        when(userRepository.findByEmail(userBefore.getEmail())).thenReturn(Optional.of(userBefore));

        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(userBefore));
    }

    @Test
    @DisplayName("поиск User по id, позитивный сценарий")
    void findUserPositiveTest() {
        Long id = 1L;

        User user = UserUtil.getUserWithRoleUser();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("поиск User по id, негативный сценарий")
    void findUserNegativeTest() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("поиск User по Email, позитивный сценарий")
    void findUserByEmailPositiveTest() {
        User user = UserUtil.getUserWithRoleUser();
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("поиск User по Email, негативный сценарий")
    void findUserByEmailNegativeTest() {
        User user = UserUtil.getUserWithRoleUser();
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("установление роли для User, позитивный сценарий")
    void setRoleToUserPositiveTest() {
        Long userId = 1L;
        Long roleId = 1L;

        User userBefore = UserUtil.getUserWithoutRoles();
        User userAfter = UserUtil.getUserWithRoleUser();
        Role role = RoleUtil.createUserRole();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userBefore));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(userAfter)).thenReturn(userAfter);

        User result = userService.setRoleToUser(userId, roleId);

        assertEquals(userAfter, result);
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRepository, times(1)).save(userAfter);
    }

    @Test
    @DisplayName("установление роли для User, негативный сценарий (User не найден)")
    void setRoleToUserNegativeTestUserNotFound() {
        Long userId = 1L;
        Long roleId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.setRoleToUser(roleId, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, never()).findById(roleId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("установление роли для User, негативный сценарий (User не найден)")
    void setRoleToUserNegativeTestRoleNotFound() {
        Long userId = 1L;
        Long roleId = 1L;

        User user = UserUtil.getUserWithoutRoles();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.setRoleToUser(roleId, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRepository, never()).save(any(User.class));
    }
}