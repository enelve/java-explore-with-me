package ru.practicum.ewm.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.ewm.main.entity.Category;

import java.util.List;

public interface CommonEndpoints {
    @RequestMapping("/categories")
    List<Category> getCategories(Integer from, Integer size);

    @RequestMapping("/categories/{catId}")
    Category getCategory(Long id);
}
