package ru.practicum.ewm.stats.client;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class StatisticClient {
    private static final String HIT = "/hit";
    private static final String STATS = "/stats";

    @Value("${stats-server.host}")
    private String host;
    RestTemplate restTemplate;

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
