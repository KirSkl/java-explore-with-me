package ru.practicum.service.category;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.SqlConflictException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;

import javax.validation.ConstraintViolationException;

@Service
@AllArgsConstructor
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
            //отловить еще одно исключение
        } catch (ConstraintViolationException e) {
            throw new SqlConflictException("The category is not empty");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        try {
            return CategoryMapper.toCategoryDto(repository.updateCategory(newCategoryDto.getName(), catId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(
                    String.format("Category with id = %s was not found", catId));
        }        //отловить второе сразу в хендлере
    }
}
