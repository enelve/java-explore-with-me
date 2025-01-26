package ru.practicum.ewm.main.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.controller.CommonEndpoints;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonController implements CommonEndpoints {
    private final CategoryService categoryService;

    @Override
    public List<Category> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @Override
    public Category getCategory(@PathVariable(value = "catId") Long id) {
        return categoryService.getCategory(id);
    }
}
