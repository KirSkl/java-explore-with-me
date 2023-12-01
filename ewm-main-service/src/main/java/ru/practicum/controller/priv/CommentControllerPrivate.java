package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentControllerPrivate {
    private final CommentService service;

    @PostMapping
    public CommentDto postComment(@PathVariable Long userId, @Valid @RequestBody NewCommentDto commentDto) {
        log.info(String.format("Получен запрос POST /users/{userId} = %s/comments на добавление комментария", userId));
        return service.postComment(userId, commentDto);
    }
}
