package com.siuuuuu.commodeami.user.query.controller;


import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.user.command.aggregate.dto.UserDTO;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import com.siuuuuu.commodeami.user.command.domain.repository.UserRepository;
import com.siuuuuu.commodeami.user.query.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseDTO<?> findMyProfile(@AuthenticationPrincipal User user) {
        String email = user.getUserName();
        UserDTO userDTO = userService.findByEmail(email);
        return ResponseDTO.ok(userDTO);
    }

    // 액세스 토큰 재발급
    @GetMapping("/refresh")
    public ResponseDTO<?> refresh(HttpServletRequest request) {
        return ResponseDTO.ok("accessToken 재발급 성공");
    }

    // 회원 조회
    @GetMapping("{userId}")
    public ResponseDTO<?> findUserById(@PathVariable("userId") Long userId) {
        return ResponseDTO.ok(userService.findById(userId));
    }
}
