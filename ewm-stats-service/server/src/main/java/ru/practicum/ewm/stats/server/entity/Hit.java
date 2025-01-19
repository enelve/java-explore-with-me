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

    @Column(name = "app", length = 255)
    private String app;

    @Column(name = "uri", length = 255)
    private String uri;

    @Column(name = "ip", length = 15)
    private String ip;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "Hit{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
