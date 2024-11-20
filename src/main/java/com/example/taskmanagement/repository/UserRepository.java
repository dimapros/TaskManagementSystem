package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT t FROM User t WHERE t.email = :email")
    Optional<User> findByEmail(String email);
}
