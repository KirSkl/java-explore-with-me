package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;

public interface CommentService {
    CommentDto postComment(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto);

    CommentDto getComment(Long commentId);

    void deleteMyComment(Long userId, Long commentId);

    void deleteCommentAdmin(Long commentId);
}
