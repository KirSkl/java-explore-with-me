package ru.practicum.common;

import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class StatsUtil {
    private final StatsClient statsClient;

    public void setEventViews(Event event) {
        var x = new ArrayList<String>();
        x.add("/events/1");
        log.info(String.format("ищем просмотры по uri = %s", x));
        List<ViewStatsDto> views = statsClient.findStats(event.getCreatedOn(), LocalDateTime.now(), true,
                List.of("/events/" + event.getId()));
        if (views.size() == 0) {
            event.setViews(0L);
        } else {
            event.setViews(views.get(0).getHits());
        }
    }
}

