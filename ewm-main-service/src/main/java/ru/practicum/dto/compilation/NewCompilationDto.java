package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@Getter
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
