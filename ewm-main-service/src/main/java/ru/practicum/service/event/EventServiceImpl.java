package ru.practicum.service.event;

import client.StatsClient;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
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
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page) {
        var events = repository.getEventsAdmin(users, states, categories, rangeStart, rangeEnd,
                page);
        events.forEach(this::setEventViews);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    private void setEventViews(Event event) {
        List<ViewStatsDto> views = statsClient.findStats(event.getCreated(), LocalDateTime.now(), true,
                List.of("/events/" + event.getId()));
        if (views.size() == 0) {
            event.setViews(0L);
        } else {
            event.setViews(views.get(0).getHits());
        }
    }
}
