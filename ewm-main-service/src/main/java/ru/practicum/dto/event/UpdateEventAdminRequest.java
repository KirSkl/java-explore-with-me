package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.EventState;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateEventAdminRequest {
    private String annotation;
    private CategoryDto category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
