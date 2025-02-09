package ru.practicum.ewm.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.*;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.model.EventState;
import ru.practicum.ewm.main.service.CategoryService;
import ru.practicum.ewm.main.service.CompilationService;
import ru.practicum.ewm.main.service.EventService;
import ru.practicum.ewm.main.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminRoleController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody @Valid UserDto userDto) {
        return userService.postUser(userDto);
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.postCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(value = "catId") Long id) {
        categoryService.deleteCategory(id);
    }

    @PatchMapping("/categories/{catId}")
    public Category patchCategory(@PathVariable(value = "catId") Long id,
                                  @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.patchCategory(id, categoryDto);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDTO addEventCompilation(@RequestBody @Valid NewCompilationDTO compilationDto) {
        return compilationService.add(compilationDto);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDTO updateEventCompilation(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest compRequest) {
        return compilationService.update(compId, compRequest);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventCompilation(@PathVariable Long compId) {
        compilationService.delete(compId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<EventState> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "rangeStart", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/events/{eventId}")
    public EventFullDto updateAdminEvent(@PathVariable("eventId") Long eventId,
                                         @RequestBody @Valid EventUpdateDTO eventUpdateDto) {
        return eventService.updateAdminEvent(eventId, eventUpdateDto);
    }
}
