package lx.team2.backend.feature.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void signup(UserVO vo) {
        vo.setPassword(new BCryptPasswordEncoder().encode(vo.getPassword()));
        userMapper.insertUser(vo);
    }
}
