package ru.prakticum.ewm.mapper;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.experimental.UtilityClass;
import ru.prakticum.ewm.model.EndpointHit;

@UtilityClass
public class HitMapper {

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
                endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp());
    }

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getId(),
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }

    public ViewStatsDto toViewStatsDto(EndpointHit endpointHit, Long hits) {
        return new ViewStatsDto(endpointHit.getApp(), endpointHit.getUri(), hits);
    }
}
