package lx.team2.backend.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody UserVO userVO) {
        userService.signup(userVO);
        return ResponseEntity.ok("회원가입 완료");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        
		// 서비스 계층의 로그인 메서드를 호출하여 사용자 인증을 시도
		int userId = userService.getUserIdByLogin(dto);

		// 인증 성공 여부 확인
		if (userId > 0) {
            // 성공 시 userId를 JSON으로 리턴
            return ResponseEntity.ok().body(userId);
        } else {
			// 로그인 실패 시 401 Unauthorized
			return ResponseEntity.status(401).body("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
		}

	}

}
