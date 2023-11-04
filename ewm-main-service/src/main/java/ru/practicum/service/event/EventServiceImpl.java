package ru.practicum.service.event;

import client.StatsClient;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exceptions.ConflictEventStateException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.model.EventState;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
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
        if (request.getAnnotation() != null) {
            oldEvent.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            oldEvent.setCategory(oldEvent.getCategory());
        }
        if (request.getDescription() != null) {
            oldEvent.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            oldEvent.setEventDate(oldEvent.getEventDate());
        }
        if (request.getLocation() != null) {
            oldEvent.setLocation(request.getLocation());
        }
        if (request.getPaid() != null) {
            oldEvent.setPaid(oldEvent.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            oldEvent.setRequestModeration(oldEvent.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case REJECT_EVENT:
                    if (oldEvent.getState().equals(EventState.PUBLISHED)) {
                        throw new ConflictEventStateException("Нельзя отменить публикацию опубликованное событие");
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
        if (request.getTitle() != null) {
            oldEvent.setTitle(oldEvent.getTitle());
        }
        return EventMapper.toEventFullDto(repository.save(oldEvent));
    }
}