package lx.team2.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    // RestTemplate타입을 받기 위해 사용
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}