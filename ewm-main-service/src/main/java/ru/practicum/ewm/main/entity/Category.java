package ru.practicum.ewm.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Accessors(chain = true)
public class Category {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Setter(value = AccessLevel.PRIVATE)
    private Long id;

    private String name;
}
