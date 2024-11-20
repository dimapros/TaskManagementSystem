package com.example.taskmanagement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введите корректное описание")
    @Size(min = 10, max = 200, message = "Описание должно состоять из минимум 10 или максимум 200 знаков")
    @NotNull(message = "Комментарий должен быть описан")
    private String content;

    @Schema(hidden = true)
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Schema(hidden = true)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
