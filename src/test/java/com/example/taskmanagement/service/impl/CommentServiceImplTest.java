package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.model.entity.Comment;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.entity.User;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.util.CommentUtil;
import com.example.taskmanagement.util.TaskUtil;
import com.example.taskmanagement.util.UserUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("добавление комментария, позитивный сценарий")
    void addCommentPositiveTest() {
        User user = UserUtil.getUserWithRoleUser();
        Task task = TaskUtil.getTask1();
        Comment comment = CommentUtil.getCommentWithAuthorAndTask();

        Long taskid = 1L;
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskid)).thenReturn(Optional.of(task));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.addComment(comment, taskid, email);

        assertEquals(result, comment);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("добавление комментария, негативный сценарий: user не найден")
    void addCommentNegativeTestUserNotFound() {
        Task task = TaskUtil.getTask1();
        Comment comment = CommentUtil.getCommentWithAuthorAndTask();

        Long taskid = 1L;
        String email = "sasha.ru.98@mail.ru";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.addComment(comment, taskid, email));

        verify(userRepository, times(1)).findByEmail(email);
        verify(taskRepository, never()).findById(taskid);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("добавление комментария, негативный сценарий: задание не найдено")
    void addCommentNegativeTestTaskNotFound() {
        User user = UserUtil.getUserWithRoleUser();
        Comment comment = CommentUtil.getCommentWithAuthorAndTask();

        Long taskid = 1L;
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskid)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.addComment(comment, taskid, email));

        verify(userRepository, times(1)).findByEmail(email);
        verify(taskRepository, times(1)).findById(taskid);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("получить комментарий, позитивный сценарий")
    void getCommentPositiveTest() {
        Long id = 1L;
        Comment comment = CommentUtil.getCommentWithAuthorAndTask();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.getComment(id);

        assertEquals(comment, result.get());
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("получить комментарий, негативный сценарий")
    void getCommentnegativeTest() {
        Long id = 1L;

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.getComment(id));
        verify(commentRepository, times(1)).findById(id);
    }
}