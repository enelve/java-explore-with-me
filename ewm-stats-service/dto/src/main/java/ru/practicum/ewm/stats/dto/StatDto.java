package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatDto {
    private String app;
    private String uri;
    private Long hits;
}
