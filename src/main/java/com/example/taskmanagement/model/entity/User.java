package com.example.taskmanagement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {
    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Email(message = "Адрес электронной почты должен быть валидным")
    @NotBlank(message = "Введите корретный Email")
    @NotNull(message = "Email обязательно")
    private String email;

    @NotBlank(message = "Введите корретный пароль")
    @Size(min = 5, max = 100, message = "Пароль должен состоять из минимум 5 или максимум 100 знаков")
    @NotNull(message = "Пароль обязателен")
    private String password;

    @Schema(hidden = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Schema(hidden = true)
    @JsonBackReference(value = "author-task")
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Task> authoredTaskEntities;

    @Schema(hidden = true)
    @JsonBackReference(value = "executor-task")
    @OneToMany(mappedBy = "executor", fetch = FetchType.LAZY)
    private List<Task> executedTaskEntities;

    @Schema(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Schema(hidden = true)
    @Override
    public String getUsername() {
        return email;
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Schema(hidden = true)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Schema(hidden = true)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
