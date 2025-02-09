package ru.practicum.ewm.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Accessors(chain = true)
public class CategoryDto {
    @NotBlank
    @Length(max = 50)
    private String name;
}
