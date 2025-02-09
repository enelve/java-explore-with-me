package ru.practicum.ewm.stats.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.server.entity.Hit;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HitController {

    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto save(@RequestBody @Valid HitDto hitDto) {
        Hit savedHit = hitService.save(hitDto);

        return HitMapper.toDto(savedHit);
    }

    @GetMapping("/stats")
    public List<StatDto> getStatistic(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        return hitService.getStatistic(start, end, uris, unique);
    }
}
