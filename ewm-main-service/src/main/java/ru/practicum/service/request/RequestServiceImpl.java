package ru.practicum.service.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.EventState;
import ru.practicum.model.RequestStatus;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
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
        checkUserIsExistsAndGet(userId);
        return repository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());

    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        var requester = checkUserIsExistsAndGet(userId);
        var event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("The event is not in the right status: PUBLISHED");
        }
        if (event.getInitiator().equals(requester)) {
            throw new DataConflictException("The requester is the initiator of the event");
        }
        if (repository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new DataConflictException("There is already a request from user to the event");
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new DataConflictException(String.format("The event has already reached its participant limit=%s",
                    event.getParticipantLimit()));
        }
        var request = new ParticipationRequestDto(null, LocalDateTime.now(), eventId, userId, RequestStatus.PENDING);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        return RequestMapper.toParticipationRequestDto(repository.save(
                RequestMapper.toParticipationRequest(request, event, requester)));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Integer requestId) {
        checkUserIsExistsAndGet(userId);
        var request = repository.findById(Long.valueOf(requestId)).orElseThrow(()
                -> new NotFoundException("Запрос не найден"));
        request.setStatus(RequestStatus.CANCELLED);
        return RequestMapper.toParticipationRequestDto(repository.save(request));
    }

    private User checkUserIsExistsAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь не найден"));
    }
}
