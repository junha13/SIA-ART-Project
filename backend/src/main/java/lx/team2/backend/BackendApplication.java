package lx.team2.backend;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "lx.team2.backend.feature")
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        // ★★★ 확인을 위해 새로운 로그를 추가해 보세요! ★★★
        //logger.info("======= Logback 테스트 성공! =======");
        //logger.warn("======= 경고(WARN) 레벨 테스트 =======");
        //logger.error("======= 에러(ERROR) 레벨 테스트 =======");
    }
}
