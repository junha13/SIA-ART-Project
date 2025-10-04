package lx.team2.backend.feature.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	
	/*
	 * 로그인
	 * id와 비밀번호가 모두 DB에 있다면 결과의 userId를 뱉는다.
	 */
	int getUserIdByLogin(@Param("loginId") String loginId, @Param("loginPw") String loginPw);
	
	/*
	 * 회원가입
	 */
    int insertUser(ResisterDTO user);

    
    /*
     * 중복확인
     */
    int checkDuplicateId(@Param("userId") String userId);
    
}
