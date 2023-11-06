package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventControllerPrivate {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getUsersEvents(@PathVariable Long userId,
                                              @PositiveOrZero @RequestParam(required = false,
                                                      defaultValue = Constants.DEFAULT_FROM) int from,
                                              @Positive @RequestParam(required = false,
                                                      defaultValue = Constants.DEFAULT_SIZE) int size) {
        log.info(String.format("Получен запрос GET /users/{userId}/events на получение списка событий, инициированных " +
                "пользователем с id = %s, начиная с %s, по %s на странице", userId, from, size));
        return service.getUserEvents(userId, PaginationUtil.toPageRequest(from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @RequestBody NewEventDto eventDto) {
        log.info(String.format("Получен запрос POST /users/{userId}/ = %s/events на добавление события", userId));
        return service.createEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto showMyEventFull(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info(String.format("Получен запрос GET /users/{userId} = %s/events/{eventId} = %s " +
                "на просмотр полной информации о событии", userId, eventId));
        return service.showMyEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateMyEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                      @RequestBody UpdateEventUserRequest request) {
        log.info(String.format("Получен запрос PATCH /users/{userId} = %s/events/{eventId} = %s " +
                "на редактирование информации о событии", userId, eventId));
        return service.updateUserEvent(userId, eventId, request);
    }
}
