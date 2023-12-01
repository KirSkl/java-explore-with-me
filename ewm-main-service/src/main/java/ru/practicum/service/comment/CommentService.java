package ru.practicum.service.comment;

import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;

public interface CommentService {
    CommentDto postComment(Long userId, NewCommentDto commentDto);
}
