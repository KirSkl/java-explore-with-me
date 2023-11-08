package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        try {
            repository.deleteById(catId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(
                    String.format("Category with id = %s was not found", catId));
        } catch (ConstraintViolationException e) {
            throw new DataConflictException("The category is not empty");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        try {
            return CategoryMapper.toCategoryDto(repository.updateCategory(newCategoryDto.getName(), catId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(
                    String.format("Category with id = %s was not found", catId));
        }
    }

    @Override
    public List<CategoryDto> getCategories(PageRequest toPageRequest) {
        return repository.findAll(toPageRequest).stream()
                .map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        var cat = repository.findById(catId).orElseThrow(()
                -> new NotFoundException(String.format("Category with id=%s was not found", catId)));
        return CategoryMapper.toCategoryDto(cat);
    }
}
