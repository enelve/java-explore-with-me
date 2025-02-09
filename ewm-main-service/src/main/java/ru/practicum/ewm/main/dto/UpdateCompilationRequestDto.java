package ru.practicum.ewm.main.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateCompilationRequestDto {

    private Set<Long> events;

    private Boolean pinned;

    @Length(min = 1, max = 50)
    private String title;
}
