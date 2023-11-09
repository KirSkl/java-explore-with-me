package ru.practicum.controller.publ;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/events")
public class EventControllerPublic {
    private final EventService service;

    @GetMapping("/{id}")
    public EventFullDto findEvent(@PathVariable Long id, HttpServletRequest request) {
        log.info(String.format("Получен запрос GET /events/{id} = %s на получение категории", id));
        return service.getEvent(id, request);
    }

    @GetMapping
    public List<EventShortDto> findAllEvents(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(required = false) Boolean onlyAvailable,
                                             @RequestParam(required = false) String sort,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size,
                                             HttpServletRequest request) {
        log.info(String.format("Получен GET /events запрос на получение списка событий с параметрами: text = %s, " +
                        "categories = %s, paid = %s, rangeStart = %s, rangeEnd = %s, onlyAvailable = %s, sort = %s, " +
                        "from = %s, size = %s", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size));
        return service.findAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                request, PaginationUtil.toPageRequest(from, size));
    }
}
