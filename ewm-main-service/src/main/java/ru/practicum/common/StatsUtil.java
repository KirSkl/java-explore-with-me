package ru.practicum.common;

import ru.practicum.StatsClient;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class StatsUtil {
    private final StatsClient statsClient;

    public void setEventViews(Event event) {
        List<ViewStatsDto> views = statsClient.findStats(event.getCreated(), LocalDateTime.now(), true,
                List.of("/events/" + event.getId()));
        if (views.size() == 0) {
            event.setViews(0L);
        } else {
            event.setViews(views.get(0).getHits());
        }
    }
}

