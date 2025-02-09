package ru.practicum.ewm.main.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserShortDto {
    private Long id;

    @NotNull
    private String name;
}
