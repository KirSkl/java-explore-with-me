package ru.practicum.controller.publ;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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

    @GetMapping(path = "{eventId}")
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @RequestParam(defaultValue = Constants.DEFAULT_FROM) @PositiveOrZero
                                             int from,
                                             @RequestParam(defaultValue = Constants.DEFAULT_SIZE) @Positive int size) {
        log.info(String.format(
                "Получен запрос GET /comments/{eventId} = %s на просмотр комментариев к событию", eventId));
        return service.getEventComment(eventId, PaginationUtil.toPageRequest(from, size));
    }


}
