package lx.team2.backend.feature.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@Slf4j
public class UserController {

	@GetMapping("/test")
	public int test() {
        log.info("======= api/test실행됨 =======");

        return 2;
	}

}
