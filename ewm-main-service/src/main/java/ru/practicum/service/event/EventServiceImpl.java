package ru.practicum.service.event;

import client.StatsClient;
import dto.EndpointHitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.EditNotAllowException;
import ru.practicum.exceptions.InvalidDatesException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsUtil statsUtil;
    private final StatsClient statsClient;

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
                        throw new EditNotAllowException("Cannot reject the event because it's in the state: PUBLISHED");
                    }
                    oldEvent.setState(EventState.CANCELLED);
                    break;
                case PUBLISH_EVENT:
                    if (!oldEvent.getState().equals(EventState.PENDING)) {
                        throw new EditNotAllowException("Cannot publish the event because " +
                                "it's not in the right state: PUBLISHED");
                    }
                    oldEvent.setState(EventState.PUBLISHED);
                    break;
            }
        }
        var updateEvent = update(oldEvent, EventMapper.toUpdateEventRequest(request));
        if (updateEvent.getState().equals(EventState.PUBLISHED) &&
                updateEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(1L))) {
            throw new InvalidDatesException(String.format("Field: eventDate. Error: Нельзя опубликовать событие " +
                    "позднее, чем за час после его начала. Value: %s", updateEvent.getEventDate()));
        }
        updateEvent = repository.save(updateEvent);
        statsUtil.setEventViews(updateEvent);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest toPageRequest) {
        checkUserIsExistsAndGet(userId);
        var events = repository.findAllByInitiatorId(userId, toPageRequest);
        events.forEach(statsUtil::setEventViews);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        checkDates(eventDto.getEventDate());
        return EventMapper.toEventFullDto(repository.save(EventMapper.toEvent(eventDto,
                checkCategoryIsExistsAndGet(eventDto.getCategory()), checkUserIsExistsAndGet(userId))));
    }

    @Override
    public EventFullDto showMyEvent(Long userId, Long eventId) {
        var user = checkUserIsExistsAndGet(userId);
        var event = checkEventIsExistsAndGet(eventId);
        statsUtil.setEventViews(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        checkUserIsExistsAndGet(userId);
        var oldEvent = checkEventIsExistsAndGet(eventId);
        if (!oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("Event must not be published");
        }
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case CANCEL_REVIEW:
                    oldEvent.setState(EventState.CANCELLED);
                    break;
                case SEND_TO_REVIEW:
                    oldEvent.setState(EventState.PENDING);
            }
        }
        var updateEvent = update(oldEvent, EventMapper.toUpdateEventRequest(request));
        checkDates(oldEvent.getEventDate());
        updateEvent = repository.save(updateEvent);
        statsUtil.setEventViews(updateEvent);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        checkUserIsExistsAndGet(userId);
        checkEventIsExistsAndGet(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }


    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        var user = checkUserIsExistsAndGet(userId);
        var event = checkEventIsExistsAndGet(eventId);
        List<Integer> ids = request.getRequestIds();
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(ids);
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            for (ParticipationRequest req : requests) {
                req.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            }
            return new EventRequestStatusUpdateResult(confirmedRequests, new ArrayList<>());
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new DataConflictException(String.format("The event has already reached its participant limit=%s",
                    event.getParticipantLimit()));
        }
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest req : requests) {
            checkRequestStatus(req);
            if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new DataConflictException(String.format("The event has already reached its participant limit=%s",
                            event.getParticipantLimit()));
                }
                req.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            } else {
                req.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            }
        }
        if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            for (ParticipationRequest req: requestRepository.findAllByStatus(RequestStatus.PENDING)) {
                req.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);

}

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        var event = repository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(()
        -> new NotFoundException(String.format("Event with id=%s was not found", id)));
        addView(request.getRequestURI(), request.getRemoteAddr());
        statsUtil.setEventViews(event);
        return EventMapper.toEventFullDto(event);
    }

    private void addView(String uri, String ip) {
        statsClient.createHit(new EndpointHitDto(null, "ewm-main-service", uri, ip, LocalDateTime.now()));
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

    private void checkDates(LocalDateTime start) {
        if (start.isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new InvalidDatesException(String.format(
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", start));
        }
    }

    private void checkRequestStatus(ParticipationRequest request) {
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new EditNotAllowException("The request is not in right status: PENDING");
        }
    }
}
