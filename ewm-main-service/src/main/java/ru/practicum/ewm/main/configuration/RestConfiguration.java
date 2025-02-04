package ru.practicum.ewm.main.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.client.StatisticClient;

@Configuration
public class RestConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StatisticClient statisticClient(@Value("${stats-server.host}") String host,
                                           RestTemplate restTemplate) {
        return new StatisticClient(host, restTemplate);
    }

}
