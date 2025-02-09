package ru.practicum.ewm.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class CommentDto {

    @NotBlank
    @Size(min = 5, max = 5000)
    private String text;

    private LocalDateTime createdOn;

    private UserShortDto user;

    private EventShortDto event;

}
