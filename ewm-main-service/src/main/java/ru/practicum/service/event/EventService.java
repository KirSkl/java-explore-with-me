package ru.practicum.service.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request);

    List<EventShortDto> getUserEvents(Long userId, PageRequest toPageRequest);

    EventFullDto createEvent(Long userId, NewEventDto eventDto);
}
