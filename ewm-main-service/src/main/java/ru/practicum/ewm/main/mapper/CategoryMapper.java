package ru.practicum.ewm.main.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.entity.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto()
                .setName(category.getName());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category()
                .setName(categoryDto.getName());
    }

    public static Category copyBusinessFields(Category category, CategoryDto categoryDto) {
        return category
                .setName(categoryDto.getName());
    }
}
