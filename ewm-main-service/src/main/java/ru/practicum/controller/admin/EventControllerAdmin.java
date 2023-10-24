package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.common.Constants;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.model.EventState;
import ru.practicum.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(defaultValue = "") List<Long> users,
                                        @RequestParam(defaultValue = "") List<EventState> states,
                                        @RequestParam(defaultValue = "") List<Long> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = Constants.DEFAULT_FROM) int from,
                                        @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size) {
        log.info(String.format("Получен запрос GET /admin/events с параметрами users=%s, states=%s, categories=%s, " +
                        "rangeStart=%s, rangeEnd=%s, from=%s, size=%s",
                users, states, categories, rangeStart, rangeEnd, from, size));
        return service.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

}
