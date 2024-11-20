package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.exception.RoleAlreadyExistsException;
import com.example.taskmanagement.model.entity.Role;
import com.example.taskmanagement.repository.RoleRepository;
import com.example.taskmanagement.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role addRole(Role role) {
        Optional<Role> optionalRole = roleRepository.findByName(role.getName());

        if (optionalRole.isPresent()) {
            throw new RoleAlreadyExistsException("Такая роль уже существует");

        }
        return roleRepository.save(role);
    }
}
