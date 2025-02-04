package ru.practicum.ewm.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.ewm.main.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class RequestDto {

    private Long id;
    private Long event;
    private Long requester;
    private RequestStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
