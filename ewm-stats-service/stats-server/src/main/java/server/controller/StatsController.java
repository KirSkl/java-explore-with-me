package java.server.controller;

import dto.HitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.server.service.StatsService;

@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }
    @PostMapping("/hit")
    public void createHit(@Valid @RequestBody HitDto hitDto) {
        log.info("Получен запрос POST /hit");
        statsService.createHit(hitDto);
    }
}
