package ru.practicum.service.compilation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

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

    @Override
    public void deleteCompilation(Integer compId) {
        if (repository.deleteByIdAndReturnCount(compId) == 0) {
            throw new NotFoundException("Подборка не найдена");
        }
    }
}
