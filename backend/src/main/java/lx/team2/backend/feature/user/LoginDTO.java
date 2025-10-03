package lx.team2.backend.feature.user;

import java.time.LocalDate;

public class LoginDTO {
	/*
	 * 정보를 넘길때 사용.
	 * DB 형태를 유출시키지 않게 하기 위해서 필요한 내용만 저장하는 용도.
	 */
	private String loginId;   // 아이디 또는 이메일
    private String loginPw;   // 비밀번호

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getLoginPw() { return loginPw; }
    public void setLoginPw(String loginPw) { this.loginPw = loginPw; }
	
}
