package ru.practicum.ewm.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.ewm.main.entity.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class EventRequestDto {
    private String annotation;
    @NotNull
    private Long category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
}
