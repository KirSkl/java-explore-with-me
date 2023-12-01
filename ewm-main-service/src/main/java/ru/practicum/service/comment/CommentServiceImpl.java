package ru.practicum.service.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto postComment(Long userId, Long eventId, NewCommentDto commentDto) {
        return CommentMapper.toCommentDto(repository.save(CommentMapper.toComment(
                checkUserIsExistsAndGet(userId), checkEventIsExistsAndGet(eventId), commentDto)));
    }

    private User checkUserIsExistsAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("User with id = %s was not found", userId)));
    }

    private Event checkEventIsExistsAndGet(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException(String.format("Event with id = %s was not found", eventId)));
    }
}
