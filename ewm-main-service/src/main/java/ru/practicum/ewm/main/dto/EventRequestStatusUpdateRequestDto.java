package ru.practicum.ewm.main.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.ewm.main.model.EventStatus;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EventRequestStatusUpdateRequestDto {

    @NotNull
    @NotEmpty
    private List<Long> requestIds;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventStatus status;
}