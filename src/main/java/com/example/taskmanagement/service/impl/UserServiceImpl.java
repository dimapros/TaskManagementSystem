package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.exception.UserAlreadyExistsException;
import com.example.taskmanagement.model.entity.Role;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.repository.RoleRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        Long userRoleId = 2L;

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException("Такой пользователь уже существует");
        }

        Role role = roleRepository.findById(userRoleId)
                .orElseThrow(EntityNotFoundException::new);

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setRoles(List.of(role));
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Пользователя не существует");

        }
        return optionalUser;
    }

    @Transactional
    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Пользователя не существует");
        }

        return optionalUser;
    }

    @Transactional
    @Override
    public User setRoleToUser(Long roleId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя не существует"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Роли не существует"));

        if (user.getRoles() == null) {
            user.setRoles(List.of(role));

        } else {
            user.getRoles().add(role);
        }

        return userRepository.save(user);
    }
}
