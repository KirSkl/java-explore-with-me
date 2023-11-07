package ru.practicum.service.event;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.ConflictEventStateException;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.EventState;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
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
        var oldEvent = checkEventIsExistsAndGet(eventId);
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
        var updateEvent = repository.save(update(oldEvent, EventMapper.toUpdateEventRequest(request)));
        statsUtil.setEventViews(updateEvent);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest toPageRequest) {
        var events = repository.findAllByInitiatorId(userId, toPageRequest);
        events.forEach(statsUtil::setEventViews);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        return EventMapper.toEventFullDto(repository.save(EventMapper.toEvent(eventDto,
                checkCategoryIsExistsAndGet(eventDto.getCategory()), checkUserIsExistsAndGet(userId))));
    }

    @Override
    public EventFullDto showMyEvent(Long userId, Long eventId) {
        var user = checkUserIsExistsAndGet(userId);
        var event = checkEventIsExistsAndGet(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new DataConflictException("Событие добавлено другим пользователем");
        }
        statsUtil.setEventViews(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        var oldEvent = checkEventIsExistsAndGet(eventId);
        if(!oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("Нельзя изменить опубликованное событие");
        }
        if(!Objects.equals(checkUserIsExistsAndGet(userId).getId(), oldEvent.getInitiator().getId())) {
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
        var updateEvent = repository.save(update(oldEvent, EventMapper.toUpdateEventRequest(request)));
        statsUtil.setEventViews(updateEvent);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        if(!checkEventIsExistsAndGet(eventId).getInitiator().getId().equals(userId)) {
            throw new DataConflictException("Событие добавлено не этим пользователем");
        };
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
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
            oldEvent.setCategory(checkCategoryIsExistsAndGet(request.getCategory()));
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

    private Event checkEventIsExistsAndGet(Long eventId) {
        return repository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено"));
    }

    private Category checkCategoryIsExistsAndGet(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()
                -> new NotFoundException("Соответствующая категория не найдена"));
    }
}
