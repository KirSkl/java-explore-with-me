package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@AllArgsConstructor
public class CompilationDto {
    private Integer id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
