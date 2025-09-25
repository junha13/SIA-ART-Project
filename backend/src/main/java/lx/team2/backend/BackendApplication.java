package lx.team2.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        // ★ 로그 테스트
        log.info("======= Spring Boot 애플리케이션 실행 성공! =======");
        log.warn("======= WARN 로그 테스트 =======");
        log.error("======= ERROR 로그 테스트 =======");
    }
}
