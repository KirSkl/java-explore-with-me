package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.comment.CommentService;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentControllerAdmin {
    private final CommentService service;

    @DeleteMapping(path = "{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        log.info(String.format(
                "Получен запрос DELETE /admin/comments/{commentId} = %s на удаление комментария", commentId));
        service.deleteCommentAdmin(commentId);
    }
}
