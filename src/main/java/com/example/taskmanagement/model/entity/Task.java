package com.example.taskmanagement.model.entity;

import com.example.taskmanagement.model.enums.Priority;
import com.example.taskmanagement.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Task {
    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(min = 5, max = 20, message = "Заголовок должен состоять из минимум 5 или максимум 20 знаков")
    @NotBlank(message = "Заголовок обязателен")
    private String title;

    @Size(min = 10, max = 500, message = "Описание должно состоять из минимум 10 или максимум 500 знаков")
    @NotBlank
    @NotNull(message = "Описание обязательно")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Статус обязателен")
    private Status taskStatus;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Приоритет обязателен")
    private Priority taskPriority;

    @Schema(hidden = true)
    @JsonManagedReference
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> commentEntities;

    @Schema(hidden = true)
    @JsonBackReference(value = "author-task")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Schema(hidden = true)
    @JsonBackReference(value = "executor-task")
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
}
