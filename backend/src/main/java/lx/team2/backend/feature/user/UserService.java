package lx.team2.backend.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
    private UserMapper userMapper;

	/*
     * 로그인
     * id와 비밀번호가 모두 DB에 있다면 결과의 userId를 뱉는다.
     */
	public Integer getUserIdByLogin(LoginDTO dto) {
        return userMapper.getUserIdByLogin(dto.getLoginId(), dto.getLoginPw());
    }
	
	/*
	 * 회원가입
	 */
    public void signup(UserVO vo) {
        vo.setPassword(new BCryptPasswordEncoder().encode(vo.getPassword()));
        userMapper.insertUser(vo);
    }
}
