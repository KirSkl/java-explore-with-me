package ru.practicum.controller;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;
    private final String timePattern = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Получен запрос POST /hit");
        statsService.createHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = timePattern) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = timePattern) LocalDateTime end,
                                       @RequestParam(defaultValue = "false") Boolean unique,
                                       @RequestParam(required = false, defaultValue = "") List<String> uris) {
        log.info(String.format("Получен запрос GET /stats с параметрами: start = %s, end = %s, unique = %s, uris = %s",
                start, end, unique, uris));
        return statsService.getStats(start, end, unique, uris);
    }
}
