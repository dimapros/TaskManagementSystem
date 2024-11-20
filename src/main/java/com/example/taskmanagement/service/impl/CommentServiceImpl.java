package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.model.entity.Comment;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Comment addComment(Comment comment, Long taskId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя не существует"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задачи с таким id не существует"));

        comment.setTask(task);
        comment.setAuthor(user);
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getComment(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isPresent()) {
            return commentOptional;
        }

        throw new EntityNotFoundException("Комментариев нет");
    }
}
