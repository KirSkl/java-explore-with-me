package ru.practicum.repository;

import dto.ViewStatsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStatsDto> getAllStatsUnique(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getAllStatsByUri(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query(value = "SELECT new dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start and :end " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStatsDto> getAllStatsByUriUnique(List<String> uris, LocalDateTime start, LocalDateTime end);
}
