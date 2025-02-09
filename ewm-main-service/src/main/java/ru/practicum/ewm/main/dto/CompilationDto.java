package ru.practicum.ewm.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class CompilationDto {

    private Integer id;

    private Set<EventShortDto> events;

    private Boolean pinned;

    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
