package ru.practicum.service;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public void createHit(EndpointHitDto endpointHitDto) {
        repository.save(HitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> urisList) {
        List<EndpointHit> hits;
        if (urisList.isEmpty()) {
            if (unique) {
                return repository.getAllStatsUnique(start, end);
            } else {
                return repository.getAllStats(start, end);
            }
        } else {
            if (unique) {
                hits = repository.getDistinctFirstByUriInAndTimestampBetween(urisList, start, end);
            } else {
                return repository.getAllStatsByUri(urisList, start, end);
            }
        }
        var countHits = hits.stream().collect(Collectors.groupingBy(
                EndpointHit::getUri, Collectors.counting()));
        Set<ViewStatsDto> viewStatsDtoSet = new HashSet<>();
        for (EndpointHit endpointHit : hits) {
            viewStatsDtoSet.add(HitMapper.toViewStatsDto(endpointHit, countHits.get(endpointHit.getUri())));
        }
        return new ArrayList<>(viewStatsDtoSet);
    }
}
