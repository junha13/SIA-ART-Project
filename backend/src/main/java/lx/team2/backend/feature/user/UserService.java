package lx.team2.backend.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
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
     * 아이디 중복확인
     * true: 중복 아님
     */
    public boolean isIdAvailable(String userId) {
        int count = userMapper.checkDuplicateId(userId);
        // count가 0이면 중복이 아니므로 true 반환
        return count == 0;
    }
	
	/*
	 * 회원가입
	 */
	public boolean signup(ResisterDTO info) {
        if (!isIdAvailable(info.getUserId())) {
            return false; // 아이디가 이미 존재하면 실패
        }
        
        // 회원 정보를 데이터베이스에 삽입, 삽입된 행의 수 반환
        int insertedRows = userMapper.insertUser(info);
        
        // 1개 행이 성공적으로 삽입되면 true 반환
        return insertedRows == 1;
    }
}
