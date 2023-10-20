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

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStatsDto> getAllStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getAllStatsByUri(Iterable uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStatsDto> getAllStatsByUriUnique(Iterable uris, LocalDateTime start, LocalDateTime end);
}
