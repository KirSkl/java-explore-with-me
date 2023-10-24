package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.Event;
import ru.practicum.service.EventService;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService service;

    @GetMapping
    public List<Event> getEvents

}
