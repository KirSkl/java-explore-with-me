package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
