package java.server.service;

import dto.HitDto;
import org.springframework.stereotype.Service;

@Service
public interface StatsService {
    void createHit(HitDto hitDto);
}
