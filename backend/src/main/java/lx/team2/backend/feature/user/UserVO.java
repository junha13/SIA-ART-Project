package lx.team2.backend.feature.user;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private String role; // "USER" or "ARTIST"
}
