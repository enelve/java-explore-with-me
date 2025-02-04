package ru.practicum.ewm.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.*;
import ru.practicum.ewm.main.service.EventService;
import ru.practicum.ewm.main.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class UserRoleController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addUserEvent(@PathVariable Long userId,
                                     @RequestBody @Valid NewEventDTO newEventDTO) {
        return eventService.addUserEvent(userId, newEventDTO);
    }

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateUserEventById(@PathVariable @NotNull Long userId,
                                            @PathVariable @NotNull Long eventId,
                                            @RequestBody @Valid EventUpdateDTO eventDto) {
        return eventService.updateUserEventById(userId, eventId, eventDto);
    }

    @GetMapping("/requests")
    public List<RequestDto> getUserRequests(@PathVariable Long userId) {
       return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addUserRequest(@PathVariable Long userId,
                                     @RequestParam(name = "eventId") Long eventId) {
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto updateUserRequest(@PathVariable Long userId,
                                        @PathVariable Long requestId) {
       return requestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> getUserEventsRequests(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
      return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequests(@PathVariable Long userId,
                                                                  @PathVariable Long eventId,
                                                                  @RequestBody EventRequestStatusUpdateRequest requestsUpdate) {
        return requestService.updateEventRequests(userId, eventId, requestsUpdate);
    }
}
