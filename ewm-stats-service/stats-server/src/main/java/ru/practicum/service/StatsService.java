package ru.practicum.service;

import dto.EndpointHitDto;
import dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> urisList);
}
