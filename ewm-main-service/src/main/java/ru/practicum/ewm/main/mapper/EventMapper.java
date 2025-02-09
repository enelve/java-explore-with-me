//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ru.practicum.ewm.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.main.dto.*;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.entity.User;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class EventMapper {
    public EventFullDto eventToEventFullDto(Event event) {
        if (event == null) {
            return null;
        } else {
            EventFullDto eventFullDto = new EventFullDto();
            eventFullDto.setId(event.getId());
            eventFullDto.setTitle(event.getTitle());
            eventFullDto.setAnnotation(event.getAnnotation());
            eventFullDto.setCategory(this.categoryToCategoryDto(event.getCategory()));
            eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
            eventFullDto.setCreatedOn(event.getCreatedOn());
            eventFullDto.setDescription(event.getDescription());
            eventFullDto.setEventDate(event.getEventDate());
            eventFullDto.setInitiator(this.userToUserShortDTO(event.getInitiator()));
            eventFullDto.setLocation(event.getLocation());
            eventFullDto.setPaid(event.getPaid());
            eventFullDto.setParticipantLimit(event.getParticipantLimit());
            eventFullDto.setPublishedOn(event.getPublishedOn());
            eventFullDto.setRequestModeration(event.getRequestModeration());
            eventFullDto.setState(event.getState());
            eventFullDto.setViews(event.getViews());
            return eventFullDto;
        }
    }

    public EventShortDto eventToShortDto(Event event) {
        if (event == null) {
            return null;
        } else {
            EventShortDto eventShortDto = new EventShortDto();
            eventShortDto.setAnnotation(event.getAnnotation());
            eventShortDto.setId(event.getId());
            eventShortDto.setCategory(this.categoryToCategoryDto(event.getCategory()));
            eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
            eventShortDto.setEventDate(event.getEventDate());
            eventShortDto.setInitiator(this.userToUserShortDTO(event.getInitiator()));
            eventShortDto.setPaid(event.getPaid());
            eventShortDto.setTitle(event.getTitle());
            eventShortDto.setViews(event.getViews());
            return eventShortDto;
        }
    }

    public Event newEventDtoToEvent(NewEventDto eventDto) {
        if (eventDto == null) {
            return null;
        } else {
            Event event = new Event();
            event.setAnnotation(eventDto.getAnnotation());
            event.setDescription(eventDto.getDescription());
            event.setEventDate(eventDto.getEventDate());
            event.setLocation(eventDto.getLocation());
            event.setPaid(eventDto.getPaid());
            if (eventDto.getParticipantLimit() != null) {
                event.setParticipantLimit(eventDto.getParticipantLimit().longValue());
            }

            event.setRequestModeration(eventDto.getRequestModeration());
            event.setTitle(eventDto.getTitle());
            return event;
        }
    }

    public Set<EventShortDto> listEventToSetEventShortDto(List<Event> events) {
        if (events == null) {
            return null;
        } else {
            Set<EventShortDto> set = new LinkedHashSet(Math.max((int) ((float) events.size() / 0.75F) + 1, 16));
            Iterator var3 = events.iterator();
            while (var3.hasNext()) {
                Event event = (Event) var3.next();
                set.add(this.eventToShortDto(event));
            }

            return set;
        }
    }

    protected CategoryDto categoryToCategoryDto(Category category) {
        if (category == null) {
            return null;
        } else {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category.getName());
            return categoryDto;
        }
    }

    protected UserShortDto userToUserShortDTO(User user) {
        if (user == null) {
            return null;
        } else {
            UserShortDto userShortDTO = new UserShortDto();
            userShortDTO.setId(user.getId());
            userShortDTO.setName(user.getName());
            return userShortDTO;
        }
    }
}
