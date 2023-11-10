package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e from Event e " +
            "WHERE (?1 is null or e.initiator.id in ?1) " +
            "AND (?2 is null or e.state in ?2) " +
            "AND (?3 is null or e.category.id in ?3) " +
            "AND e.eventDate > ?4 " +
            "AND e.eventDate < ?5")
    List<Event> getEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable page);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (?1 is null or upper(e.annotation) LIKE upper(concat('%', ?1, '%')) OR upper(e.description) " +
            "LIKE upper(concat('%', ?1, '%')))" +
            "AND (?2 is null OR e.category.id in ?2) " +
            "AND (?3 is null OR e.paid = ?3) " +
            "AND e.eventDate > ?4 " +
            "AND e.eventDate < ?5 " +
            "and (?6 is null or e.confirmedRequests < e.participantLimit or e.participantLimit = 0)")
    List<Event> findAllPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable page);

}

