package server.service;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.mapper.HitMapper;
import server.model.EndpointHit;
import server.repository.StatsRepository;

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
                hits = repository.getDistinctByTimestampBetween(start, end);
            } else {
                hits = repository.getAllByTimestampBetween(start, end);
            }
        } else {
            if (unique) {
                hits = repository.getDistinctByUriInAndTimestampBetween(urisList, start, end);
            } else {
                hits = repository.getAllByUriInAndTimestampBetween(urisList, start, end);
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
