package com.example.taskmanagement.util;

import com.example.taskmanagement.model.entity.Comment;
import com.example.taskmanagement.model.entity.Task;
import com.example.taskmanagement.model.entity.User;

public class CommentUtil {
    public static Comment createComment(String content, User author, Task task) {
        Comment comment = new Comment();

        comment.setContent(content);
        comment.setAuthor(author);
        comment.setTask(task);

        return comment;
    }

    public static Comment getCommentWithoutAuthorAndTask() {
        return createComment("Исправил задачу", null, null);
    }

    public static Comment getCommentWithAuthorAndTask() {
        return createComment("Исправил задачу", UserUtil.getUserWithRoleUser(), TaskUtil.getTask1());
    }
}
