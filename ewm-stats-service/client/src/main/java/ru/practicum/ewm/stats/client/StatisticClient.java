package ru.practicum.ewm.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticClient {
    private static final String HIT = "/hit";
    private static final String STATS = "/stats";

    private final String host;
    private final RestTemplate restTemplate;

    public StatisticClient(@Value("${stats-server.host}") String host, RestTemplate restTemplate) {
        this.host = host;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<HitDto> save(HitDto hitDto) {
        String url = host + HIT;

        return restTemplate.postForEntity(url, hitDto, HitDto.class);
    }

    public ResponseEntity<StatDto[]> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique
    ) {
        String url = host + STATS + "?start={start}&end={end}&uris={uris}&unique={unique}";

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", uris);
        params.put("unique", unique);

        return restTemplate.getForEntity(url, StatDto[].class, params);
    }
}
