package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ParticipationRequestDto {
    private Integer id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
