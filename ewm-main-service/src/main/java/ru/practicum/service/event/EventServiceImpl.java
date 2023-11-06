package ru.practicum.service.event;

import client.StatsClient;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.event.*;
import ru.practicum.exceptions.ConflictEventStateException;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.model.EventState;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsUtil statsUtil;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page) {
        var events = repository.getEventsAdmin(users, states, categories, rangeStart, rangeEnd,
                page);
        events.forEach(statsUtil::setEventViews);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request) {
        var oldEvent = repository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено"));
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case REJECT_EVENT:
                    if (oldEvent.getState().equals(EventState.PUBLISHED)) {
                        throw new ConflictEventStateException("Нельзя отменить публикацию опубликованное события");
                    }
                    oldEvent.setState(EventState.CANCELLED);
                    break;
                case PUBLISH_EVENT:
                    if (!oldEvent.getState().equals(EventState.PENDING)) {
                        throw new ConflictEventStateException("Опубликовать можно только событие, ожидающее публикации");
                    }
                    oldEvent.setState(EventState.PUBLISHED);
                    break;
            }
        }
        return EventMapper.toEventFullDto(repository.save(update(oldEvent, EventMapper.toUpdateEventRequest(request))));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest toPageRequest) {
        return repository.findAllByInitiatorId(userId, toPageRequest).stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        var cat = categoryRepository.findById(Long.valueOf(eventDto.getCategory())).orElseThrow(()
        -> new NotFoundException("Соответствующая категория не найдена"));
        var initiator = checkUserIsExistsAndGet(userId);
        return EventMapper.toEventFullDto(repository.save(EventMapper.toEvent(eventDto, cat, initiator)));
    }

    @Override
    public EventFullDto showMyEvent(Long userId, Long eventId) {
        var user = checkUserIsExistsAndGet(userId);
        var event = repository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!event.getInitiator().equals(user)) {
            throw new DataConflictException("Событие добавлено другим пользователем");
        }
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        var oldEvent = repository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено"));
        if(!oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("Нельзя изменить опубликованное событие");
        }
        if(checkUserIsExistsAndGet(userId).getId() != oldEvent.getInitiator().getId()) {
            throw new DataConflictException("Изменить можно только свое событие");
        }
        if(request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case CANCEL_REVIEW:
                    oldEvent.setState(EventState.CANCELLED);
                    break;
                case SEND_TO_REVIEW:
                    oldEvent.setState(EventState.PENDING);
            }
        }
        return EventMapper.toEventFullDto(repository.save(update(oldEvent, EventMapper.toUpdateEventRequest(request))));
    }

    private User checkUserIsExistsAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь не найден"));
    }

    private Event update(Event oldEvent, UpdateEventRequest request) {
        if (request.getAnnotation() != null) {
            oldEvent.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            oldEvent.setCategory(categoryRepository.findById(Long.valueOf(request.getCategory()))
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (request.getDescription() != null) {
            oldEvent.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            oldEvent.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            oldEvent.setLocation(request.getLocation());
        }
        if (request.getPaid() != null) {
            oldEvent.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            oldEvent.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            oldEvent.setTitle(request.getTitle());
        }
        return oldEvent;
    }
}
