package lx.team2.backend.feature.user;

public class RegisterDTO {
    private String userId;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private int userType; // 1: 사용자, 2: 예술가
    private String selectedInterests; // 선택한 관심사들 (콤마로 구분된 문자열)
    
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserType() {
		return userType;
	}

	// Setter accepting int
	public void setUserType(int userType) {
		this.userType = userType;
	}

	// Setter accepting String (to handle frontend sending "1"/"2")
	public void setUserType(String userType) {
		try {
			this.userType = Integer.parseInt(userType);
		} catch (NumberFormatException e) {
			this.userType = 0;
		}
	}

	public String getSelectedInterests() {
		return selectedInterests;
	}

	public void setSelectedInterests(String selectedInterests) {
		this.selectedInterests = selectedInterests;
	}
	
	
    
    
}
