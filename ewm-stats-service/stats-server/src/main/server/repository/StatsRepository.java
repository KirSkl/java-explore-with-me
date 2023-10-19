package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> getAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> getDistinctByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> getAllByUriInAndTimestampBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    List<EndpointHit> getDistinctByUriInAndTimestampBetween(List<String> uris, LocalDateTime start,
                                                            LocalDateTime end);

}
