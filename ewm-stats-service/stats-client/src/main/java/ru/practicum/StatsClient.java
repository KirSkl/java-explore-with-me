package ru.practicum;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient {
    private final RestTemplate rest;

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void createHit(EndpointHitDto dto) {
        rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(dto, defaultHeaders()), Object.class)
                .getStatusCodeValue();
    }

    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        String encodedStartTime = URLEncoder.encode(start.toString(), StandardCharsets.UTF_8);
        String endStartTime = URLEncoder.encode(end.toString(), StandardCharsets.UTF_8);
        Map<String, Object> parameters = Map.of(
                "start", encodedStartTime,
                "end", endStartTime,
                "unique", unique,
                "uris", uris
        );
        return rest.exchange("/stats?start={start}&end={end}&unique={unique}", HttpMethod.GET,
                new HttpEntity<>(defaultHeaders()), new ParameterizedTypeReference<List<ViewStatsDto>>() {
                }, parameters).getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}