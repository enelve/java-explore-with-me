package ru.practicum.ewm.main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "\"User\"")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Setter(value = AccessLevel.PRIVATE)
    private Long id;

    @Email
    private String email;

    private String name;
}
