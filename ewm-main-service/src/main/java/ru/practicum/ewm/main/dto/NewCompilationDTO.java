package ru.practicum.ewm.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class NewCompilationDTO {

    private Set<Long> events;

    private Boolean pinned = false;

    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
