package ru.practicum.dto.request;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private java.util.List<ParticipationRequestDto> rejectedRequests;
}
