package com.example.taskmanagement.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Schema(hidden = true)
    @Override
    public String getAuthority() {
        return name;
    }
}
