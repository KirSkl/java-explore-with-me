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
            "WHERE (:users is null or e.initiator.id in :users) " +
            "AND (:states is null or e.state in :states) " +
            "AND (:categories is null or e.category.id in :categories) " +
            "AND (coalesce(:rangeStart, null) is null or e.eventDate > :rangeStart) " +
            "AND (coalesce(:rangeEnd, null) is null or e.eventDate < :rangeEnd)")
    List<Event> getEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable page);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query("select e from Event e " +
            "where e.state = 'PUBLISHED' " +
            "and (:text is null or upper(e.annotation) like upper(concat('%', :text, '%')) or upper(e.description) " +
            "like upper(concat('%', :text, '%')))" +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and e.eventDate > :rangeStart " +
            "and e.eventDate < :rangeEnd " +
            "and (:onlyAvailable is null or e.confirmedRequests < e.participantLimit or e.participantLimit = 0)")
    List<Event> findAllPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable page);

}

