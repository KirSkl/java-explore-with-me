package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentControllerPrivate {
    private final CommentService service;

    @PostMapping(path = "/{eventId}")
    public CommentDto postComment(@PathVariable Long userId, @PathVariable Long eventId,
                                  @Valid @RequestBody NewCommentDto commentDto) {
        log.info(String.format(
                "Получен запрос POST /users/{userId} = %s/comments/{eventId} = %s на добавление комментария",
                userId, eventId));
        return service.postComment(userId, eventId, commentDto);
    }

    @PatchMapping(path = "{commentId}")
    public CommentDto patchComment(@PathVariable Long userId, @PathVariable Long commentId,
                                   @Valid @RequestBody UpdateCommentDto commentDto) {
        log.info(String.format(
                "Получен запрос PATCH /users/{userId} = %s/comments/{commentId} = %s на изменение комментария",
                userId, commentId));
        return service.updateComment(userId, commentId, commentDto);
    }

    @GetMapping(path = "{commentId}")
    public CommentDto getMyComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info(String.format(
                "Получен запрос PATCH /users/{userId} = %s/comments/{commentId} = %s на просмотр своего комментария",
                userId, commentId));
        return service.getComment(userId);
    }
}
