package com.example.taskmanagement.util;

import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.model.enums.Priority;
import com.example.taskmanagement.model.enums.Status;

public class TaskUtil {

    public static Task createTask(String title,
                                  String description,
                                  Status taskStatus,
                                  Priority taskPriority,
                                  User author,
                                  User executor) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskStatus(taskStatus);
        task.setTaskPriority(taskPriority);
        task.setAuthor(author);
        task.setExecutor(executor);
        return task;
    }

    public static Task createTask(String title,
                                  String description,
                                  Status taskStatus,
                                  Priority taskPriority,
                                  User author) {
        return createTask(title, description, taskStatus, taskPriority, author, null);
    }

    public static Task createTask(String title,
                                  String description,
                                  Status taskStatus,
                                  Priority taskPriority) {
        return createTask(title, description, taskStatus, taskPriority, null, null);
    }

    public static User getAuthor() {
        return UserUtil.createAuthor("author@mail.ru", "password");
    }

    public static User getExecutor() {
        return UserUtil.createExecutor("executor@mail.ru", "password");
    }

    public static Task getTask1() {
        return createTask("Пагинация", "Выполнить пагинацию", Status.WAITING, Priority.HIGH, getAuthor(), getExecutor());
    }

    public static Task getTask2() {
        return createTask("JWT токен", "Сделать JWT", Status.WAITING, Priority.LOW, getAuthor(), getExecutor());
    }

    public static Task getTaskWithoutExecutor() {
        return createTask("Валидация", "Добавить валидацию", Status.WAITING, Priority.MEDIUM, getAuthor());
    }

    public static Task getTaskWithExecutor() {
        return createTask("Валидация", "Добавить валидацию", Status.WAITING, Priority.MEDIUM, getAuthor(), getExecutor());
    }

    public static Task getTaskWithoutAuthor() {
        return createTask("Изменить таблицу", "Добавить значение в таблицу", Status.WAITING, Priority.MEDIUM);
    }

    public static Task getTaskChangedStatusPriority() {
        return createTask("Пагинация", "Выполнить пагинацию", Status.RUNNING, Priority.MEDIUM, getAuthor(), getExecutor());
    }

    public static Task getTaskChangedStatus() {
        return createTask("Пагинация", "Выполнить пагинацию", Status.RUNNING, Priority.HIGH, getAuthor(), getExecutor());
    }
}
