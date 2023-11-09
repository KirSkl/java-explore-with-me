package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.model.Location;
import ru.practicum.model.StateUserAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateUserAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
