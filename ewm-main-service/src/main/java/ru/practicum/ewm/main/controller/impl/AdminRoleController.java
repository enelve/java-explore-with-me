package ru.practicum.ewm.main.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.controller.AdminRoleEndpoints;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.service.CategoryService;
import ru.practicum.ewm.main.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminRoleController implements AdminRoleEndpoints {
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody @Valid UserDto userDto) {
        return userService.postUser(userDto);
    }

    @Override
    public List<User> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long id) {
        userService.deleteUser(id);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Category postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.postCategory(categoryDto);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(value = "catId") Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    public Category patchCategory(@PathVariable(value = "catId") Long id,
                                  @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.patchCategory(id, categoryDto);
    }
}
