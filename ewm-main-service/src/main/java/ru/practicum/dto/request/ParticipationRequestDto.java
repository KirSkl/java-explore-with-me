package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.RequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ParticipationRequestDto {
    private Integer id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
