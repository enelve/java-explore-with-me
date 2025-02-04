package ru.practicum.ewm.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.Accessors;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Setter(value = AccessLevel.PRIVATE)
    private Long id;

    @Email
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    private String name;
}
