package ru.practicum.ewm.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Setter(value = AccessLevel.PRIVATE)
    @JsonIgnore
    private Long id;
    private Float lat;
    private Float lon;
}
