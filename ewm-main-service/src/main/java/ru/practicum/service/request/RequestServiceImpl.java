package ru.practicum.service.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.EventState;
import ru.practicum.model.RequestStatus;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        return repository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());

    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        var requester = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь не найден"));
        var event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException(String.format("The event by the id=%d is not published", event.getId()));
        }
        if (event.getInitiator().equals(requester)) {
            throw new DataConflictException(String.format("The requester by the id=%d is the initiator of the event " +
                    "by the id=%d", requester.getId(), event.getId()));
        }
        if (repository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new DataConflictException(String.format("There is already a request from this user by the id=%d to " +
                    "the event by the id=%d", userId, eventId));
        }
        if (event.getConfirmedRequests() + 1 > event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new DataConflictException(String.format("The event by the id=%d has already reached its " +
                    "participant limit=%d", event.getId(), event.getParticipantLimit()));
        }
        var request = new ParticipationRequestDto(null, LocalDateTime.now(), eventId, userId, RequestStatus.PENDING);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        return RequestMapper.toParticipationRequestDto(repository.save(
                RequestMapper.toParticipationRequest(request, event, requester)));
    }
}
