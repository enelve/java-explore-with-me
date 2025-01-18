package ru.practicum.ewm.stats.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
public class StatDto {
    @NotNull
    @Length(max = 255)
    private String app;

    @NotNull
    @Length(max = 255)
    private String uri;

    @NotNull
    private Long hits;
}
