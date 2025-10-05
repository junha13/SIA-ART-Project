package lx.team2.backend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Value("${naver.api.clientId}")
    private String naverClientId;

    @Value("${naver.api.clientSecret}")
    private String naverClientSecret;

    // 기본 RestTemplate (일반 HTTP 호출용)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 네이버 API 전용 RestTemplate (모든 요청에 자동으로 API Key 헤더 추가)
    @Bean
    public RestTemplate naverRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().set("x-ncp-apigw-api-key-id", naverClientId);
            request.getHeaders().set("x-ncp-apigw-api-key", naverClientSecret);
            return execution.execute(request, body);
        };

        restTemplate.setInterceptors(List.of(interceptor));
        return restTemplate;
    }
}
