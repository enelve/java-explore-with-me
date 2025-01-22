package ru.practicum.ewm.stats.server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.server.entity.Hit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    public static HitDto toDto(Hit hit) {
        return new HitDto()
                .setApp(hit.getApp())
                .setUri(hit.getUri())
                .setIp(hit.getIp())
                .setTimestamp(hit.getTimestamp());
    }

    public static Hit toHit(HitDto hitDto) {
        return new Hit()
                .setApp(hitDto.getApp())
                .setUri(hitDto.getUri())
                .setIp(hitDto.getIp())
                .setTimestamp(hitDto.getTimestamp());
    }
}
