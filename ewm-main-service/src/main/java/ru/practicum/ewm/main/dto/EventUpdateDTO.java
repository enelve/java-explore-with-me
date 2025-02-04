package ru.practicum.ewm.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.ewm.main.entity.Location;
import ru.practicum.ewm.main.model.StateAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class EventUpdateDTO {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Long participantLimit;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private StateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
