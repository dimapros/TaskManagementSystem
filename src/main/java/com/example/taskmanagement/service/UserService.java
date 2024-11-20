package com.example.taskmanagement.service;

import com.example.taskmanagement.model.entity.User;

import java.util.Optional;

public interface UserService {
    User addUser(User executor);

    Optional<User> getUserByEmail(String email);

    User setRoleToUser(Long roleId, Long userId);

    Optional<User> getUserById(Long executorId);
}
