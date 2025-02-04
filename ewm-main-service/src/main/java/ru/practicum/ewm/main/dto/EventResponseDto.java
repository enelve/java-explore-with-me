package ru.practicum.ewm.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.ewm.main.entity.Category;
import ru.practicum.ewm.main.entity.Location;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.model.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class EventResponseDto {
    private String annotation;
    private Category category;
    private int confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private User initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private EventState state;
    private String title;
    private int views;
}
