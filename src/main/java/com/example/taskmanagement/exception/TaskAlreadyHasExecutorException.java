package com.example.taskmanagement.exception;

public class TaskAlreadyHasExecutorException extends RuntimeException {
    public TaskAlreadyHasExecutorException(String message) {
        super(message);
    }
}
