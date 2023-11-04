package ru.practicum.service.compilation;

import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;

@Service
public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto compilationDto);
}
