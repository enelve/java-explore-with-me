package ru.practicum.ewm.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.mapper.CategoryMapper;
import ru.practicum.ewm.main.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category with id=%d was not found";
    private final CategoryRepository categoryRepository;

    public Category postCategory(CategoryDto categoryDto) {
        return categoryRepository.save(CategoryMapper.toCategory(categoryDto));
    }

    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        () -> {
                            throw new NotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, id));
                        });
    }

    public Category patchCategory(Long id, CategoryDto categoryDto) {
        Category patchedCategory = categoryRepository.findById(id)
                .map(category -> CategoryMapper.copyBusinessFields(category, categoryDto))
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, id)));

        return categoryRepository.save(patchedCategory);
    }

    public List<Category> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);

        return categoryRepository.findAll(pageable).getContent();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, id)));
    }
}
