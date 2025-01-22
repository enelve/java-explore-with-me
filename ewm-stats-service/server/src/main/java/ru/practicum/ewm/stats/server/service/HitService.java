package ru.practicum.ewm.stats.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.server.entity.Hit;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitService {
    private final HitRepository hitRepository;

    public Hit save(HitDto hitDto) {
        return hitRepository.save(HitMapper.toHit(hitDto));
    }

    public List<StatDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatDto> result = (unique)
                ? hitRepository.findUnique(start, end)
                : hitRepository.findAll(start, end);

        if (uris != null && !uris.isEmpty()) {
            return result.stream().filter(statDto -> uris.contains(statDto.getUri())).toList();
        }

        return result;
    }
}
