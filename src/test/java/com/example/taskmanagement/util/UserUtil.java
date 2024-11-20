package com.example.taskmanagement.util;

import com.example.taskmanagement.model.entity.Role;
import com.example.taskmanagement.model.entity.User;

import java.util.Collections;
import java.util.List;

public class UserUtil {

    public static User createUser(String email, String password, List<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }

    public static User createUserWithoutRoles(String email, String password) {
        Role defaultRole = new Role();
        return createUser(email, password, null);
    }

    public static User createExecutor(String email, String password) {
        Role defaultRole = new Role();
        defaultRole.setName("ROLE_USER");
        return createUser(email, password, Collections.singletonList(defaultRole));
    }

    public static User createAuthor(String email, String password) {
        Role defaultRole = new Role();
        defaultRole.setName("ROLE_ADMIN");
        return createUser(email, password, Collections.singletonList(defaultRole));
    }

    public static User getUserWithRoleUser() {
        return createExecutor("user@mail.ru", "password");
    }

    public static User getUserWithRoleAdmin() {
        return createAuthor("admin@mail.ru", "password");
    }

    public static User getUserAfterChangePassword() {
        return createExecutor("user@mail.ru", "hashedPassword");
    }

    public static User getUserWithoutRoles() {
        Role defaultRole = new Role();
        return createUserWithoutRoles("user@mail.ru", "password");
    }
}
