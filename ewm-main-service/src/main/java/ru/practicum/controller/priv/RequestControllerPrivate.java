package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users/")
public class RequestControllerPrivate {
    private final RequestService service;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findRequests(@PathVariable Long userId) {
        log.info(String.format("Получен запрос GET /users/{userId} = %s /requests"));
        return service.getRequests(userId);
    }
}
