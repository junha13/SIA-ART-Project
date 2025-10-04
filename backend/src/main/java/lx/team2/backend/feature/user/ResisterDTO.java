package lx.team2.backend.feature.user;

public class ResisterDTO {
    private String userId;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private int userType; // 1: 사용자, 2: 예술가
    private String selectedInterests; // 선택한 관심사들 (콤마로 구분된 문자열)
}
