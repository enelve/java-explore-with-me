package ru.practicum.ewm.main.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.entity.User;

import java.util.List;

@RequestMapping("/admin")
public interface AdminRoleEndpoints {

    @PostMapping("/users")
    User postUser(UserDto userDto);

    @GetMapping("/users")
    List<User> getUsers(List<Long> ids, Integer from, Integer size);

    @DeleteMapping("/users/{userId}")
    void deleteUser(Long id);

    @PostMapping("/categories")
    Category postCategory(CategoryDto categoryDto);

    @DeleteMapping("/categories/{catId}")
    void deleteCategory(Long id);

    @PatchMapping("/categories/{catId}")
    Category patchCategory(Long id, CategoryDto categoryDto);
}
