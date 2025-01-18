package ru.practicum.ewm.stats.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "hits", schema = "public")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "app", length = 255)
    private String app;

    @NotNull
    @Column(name = "uri", length = 255)
    private String uri;

    @NotNull
    @Column(name = "ip", length = 15)
    private String ip;

    @NotNull
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
