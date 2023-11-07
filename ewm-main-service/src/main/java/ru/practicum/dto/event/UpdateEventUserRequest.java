package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;
import ru.practicum.model.StateUserAction;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateEventUserRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateUserAction stateAction;
    private String title;
}
