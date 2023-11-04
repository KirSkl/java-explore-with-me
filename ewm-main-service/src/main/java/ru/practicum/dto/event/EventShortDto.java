package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@AllArgsConstructor
public class EventShortDto {
    private Long id;
    private String description;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
