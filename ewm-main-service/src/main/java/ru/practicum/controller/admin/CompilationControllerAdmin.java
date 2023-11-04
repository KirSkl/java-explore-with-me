package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/compilation")
public class CompilationControllerAdmin {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Получен запрос POST /admin/compilation");
        return service.addCompilation(compilationDto);
    }
}
