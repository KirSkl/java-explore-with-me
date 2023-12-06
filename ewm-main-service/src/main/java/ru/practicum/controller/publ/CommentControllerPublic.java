package ru.practicum.controller.publ;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.comment.CommentService;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/comments")
public class CommentControllerPublic {
    private final CommentService service;

    @GetMapping(path = "{commentId}")
    public CommentDto getComment(@PathVariable Long commentId) {
        log.info(String.format(
                "Получен запрос GET /comments/{commentId} = %s на просмотр своего комментария", commentId));
        return service.getComment(commentId);
    }
}
