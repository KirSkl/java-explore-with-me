package ru.practicum.service.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.exceptions.EditNotAllowException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto postComment(Long userId, Long eventId, NewCommentDto commentDto) {
        return CommentMapper.toCommentDto(repository.save(CommentMapper.toComment(
                checkUserExistsAndGet(userId), checkEventExistsAndGet(eventId), commentDto)));
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto) {
        checkUserExistsAndGet(userId);
        var oldComment = checkCommentExistsAndGet(commentId);
        if (!oldComment.getAuthor().getId().equals(userId)) {
            throw new EditNotAllowException("You can edit your comments only");
        }
        if (oldComment.getMeaning().equals(commentDto.getUpdateMeaning())) {
            return CommentMapper.toCommentDto(oldComment);
        }
        oldComment.setMeaning(commentDto.getUpdateMeaning());
        oldComment.setUpdated(LocalDateTime.now());
        return CommentMapper.toCommentDto(repository.save(oldComment));
    }

    @Override
    public CommentDto getComment(Long commentId) {
        return CommentMapper.toCommentDto(checkCommentExistsAndGet(commentId));
    }

    private User checkUserExistsAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("User with id = %s was not found", userId)));
    }

    private Event checkEventExistsAndGet(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException(String.format("Event with id = %s was not found", eventId)));
    }

    private Comment checkCommentExistsAndGet(Long commentId) {
        return repository.findById(commentId).orElseThrow(()
                -> new NotFoundException(String.format("Comment with id = %s was not found", commentId)));
    }
}
