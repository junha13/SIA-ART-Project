package lx.team2.backend.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
    private UserService userService;

	/**
     * 아이디 중복확인 API입니다.
     * @param signupDto 클라이언트가 보낸 loginId를 담은 객체
     * @return 사용 가능 시 'available', 중복 시 'duplicate' 문자열 응답
     */
    @GetMapping("/check-id")
    public ResponseEntity<String> checkDuplicate(@RequestBody RegisterDTO user) {
        // 서비스 계층을 통해 아이디 사용 가능 여부 확인
        boolean isAvailable = userService.isIdAvailable(user.getUserId());
        
        if (isAvailable) {
            return ResponseEntity.ok("available");
        } else {
            return ResponseEntity.ok("duplicate");
        }
    }

    /**
     * 회원가입 처리 API입니다.
     * @param signupDto 클라이언트가 보낸 회원 정보를 담은 객체
     * @return 회원가입 성공 시 'success', 실패 시 'fail' 문자열 응답
     */
    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody RegisterDTO user) {
        // 서비스 계층을 통해 회원가입 로직 수행
        boolean isSuccess = userService.register(user);
        
        if (isSuccess) {
            return ResponseEntity.ok("success");
        } else {
            // 중복된 아이디 또는 기타 이유로 실패 시
            return ResponseEntity.ok("fail");
        }
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
