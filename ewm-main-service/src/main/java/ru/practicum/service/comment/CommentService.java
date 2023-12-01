package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;

public interface CommentService {
    CommentDto postComment(Long userId, Long eventId, NewCommentDto commentDto);
}
