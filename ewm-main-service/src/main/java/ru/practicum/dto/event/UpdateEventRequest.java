package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
