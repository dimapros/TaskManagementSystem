package com.example.taskmanagement.util;

import com.example.taskmanagement.model.entity.Role;

public class RoleUtil {
    public static Role createRole(String name) {
        Role role = new Role();
        role.setName(name);

        return role;
    }

    public static Role createUserRole() {
        return createRole("ROLE_USER");
    }

    public static Role createAdminRole() {
        return createRole("ROLE_ADMIN");
    }
}
