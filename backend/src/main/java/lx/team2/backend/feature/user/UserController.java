package lx.team2.backend.feature.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class UserController {

	@GetMapping("/test")
	public int test() {
		return 2;
	}
}
