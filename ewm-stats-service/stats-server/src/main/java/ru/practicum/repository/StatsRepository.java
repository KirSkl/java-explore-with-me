package ru.practicum.repository;

import dto.ViewStatsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> getAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> getDistinctFirstByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> getAllByUriInAndTimestampBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    List<EndpointHit> getDistinctFirstByUriInAndTimestampBetween(List<String> uris, LocalDateTime start,
                                                                 LocalDateTime end);

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, count(h.ip)) " +
            "FROM EndpointHit as h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.uri, h.app")
    List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, count(DISTINCT h.ip)) " +
            "FROM EndpointHit as h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.uri, h.app")
    List<ViewStatsDto> getAllStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, count(h.ip)) " +
            "FROM EndpointHit as h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.uri, h.app")
    List<ViewStatsDto> getAllStatsByUri(Iterable uris, LocalDateTime start, LocalDateTime end);
}
