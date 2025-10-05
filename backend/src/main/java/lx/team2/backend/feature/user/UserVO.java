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
	
	private int userNumber; // user_number
	private String id;
    private String password;
    private String userName; // user_name
	private String activityName; // activity_name
	private String mobile;
	private String email;
	private LocalDate birthday;
    private int roleNumber; // role_number, 1: 사용자, 2: 여술가
    private double locationLon; // location 경도
	private double locationLat; // location 위도
	private String interestsList; // interests_list
	
}
