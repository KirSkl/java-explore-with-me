package ru.practicum.service.compilation;

import client.StatsClient;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final StatsUtil statsUtil;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        var events = eventRepository.findAllById(compilationDto.getEvents());
        events.forEach(statsUtil::setEventViews);
        return CompilationMapper.toCompilationDto(
                repository.save(CompilationMapper.toCompilation(compilationDto, events)));
    }
}
