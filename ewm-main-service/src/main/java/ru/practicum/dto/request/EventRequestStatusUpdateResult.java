package ru.practicum.dto.request;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
