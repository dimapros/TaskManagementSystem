package com.example.taskmanagement.service;

import com.example.taskmanagement.model.entity.Comment;

import java.util.Optional;

public interface CommentService {
    Comment addComment(Comment comment, Long taskId, String email);

    Optional<Comment> getComment(Long id);
}
