package ru.practicum.service;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.InvalidDatesException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public void createHit(EndpointHitDto endpointHitDto) {
        log.info(String.format("В базу данных будет добавлен статхит c uri = %s", endpointHitDto.getUri()));
        repository.save(HitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, String[] urisList) {
        if (urisList.length == 0) {
            if (unique) {
                return repository.getAllStatsUnique(start, end);
            } else {
                return repository.getAllStats(start, end);
            }
        } else {
            var uris = String.join(",", urisList);
            if (unique) {
                log.info(String.format("В базе данных будет выполнен поиск по uri = %s", urisList[0]));
                return repository.getAllStatsByUriUnique(urisList, start, end);
            } else {
                return repository.getAllStatsByUri(urisList, start, end);
            }
        }
    }
}
