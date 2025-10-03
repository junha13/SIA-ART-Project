package lx.team2.backend.feature.user;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserVO {
	/*
	 * DB와 같은 형식을 취함.
	 * backend 내부에서 정보들을 저장하고 옮김.
	 * 테이블의 모든 정보를 포함함
	 */
	
	private int userId;
	private String id;
    private String username;
    private String password;
    private String role; // "USER" or "ARTIST"
	private String penName;
	private String mobile;
	private String email;
	private LocalDate birth;
	private int locationId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPenName() {
		return penName;
	}
	public void setPenName(String penName) {
		this.penName = penName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getBirth() {
		return birth;
	}
	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
    
    
}
