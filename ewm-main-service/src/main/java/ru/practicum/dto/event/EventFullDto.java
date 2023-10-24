package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.EventState;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private Category category;
    private LocalDateTime created;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime published;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;
    private Integer confirmedRequests;
}
