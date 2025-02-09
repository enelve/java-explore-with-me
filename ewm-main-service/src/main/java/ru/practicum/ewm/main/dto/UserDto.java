package ru.practicum.ewm.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Accessors(chain = true)
public class UserDto {
    @NotNull
    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    private String email;
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
}
