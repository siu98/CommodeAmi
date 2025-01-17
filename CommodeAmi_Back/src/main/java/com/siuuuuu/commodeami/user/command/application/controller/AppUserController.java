package com.siuuuuu.commodeami.user.command.application.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.user.command.aggregate.dto.UserDTO;
import com.siuuuuu.commodeami.user.command.aggregate.vo.PwdChangeRequestVO;
import com.siuuuuu.commodeami.user.command.aggregate.vo.RegistRequestVO;
import com.siuuuuu.commodeami.user.command.application.service.AppUserService;
import com.siuuuuu.commodeami.user.command.application.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService userService;
    private final ModelMapper modelMapper;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public AppUserController(AppUserService userService,
                             ModelMapper modelMapper, EmailVerificationService emailVerificationService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.emailVerificationService = emailVerificationService;
    }

    // 회원 가입
    @PostMapping("/regist")
    public ResponseDTO<?> registNewUser(@RequestBody RegistRequestVO requestVO) {

        UserDTO newUser = modelMapper.map(requestVO, UserDTO.class);
        userService.registUser(newUser);

        return ResponseDTO.ok(newUser);
    }

    // 비밀번호 수정
    @PutMapping("/password/{userId}")
    public ResponseDTO<?> updatePassword(@PathVariable("userId") Long userId,
                                         @Valid @RequestBody PwdChangeRequestVO request) {
        log.info("비밀번호 수정 요청 들어옴");
        userService.updatePassword(userId, request.getCurrentPwd(), request.getNewPwd());

        return ResponseDTO.ok("비밀번호가 변경되었습니다.");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseDTO<?> loggout() {

        return ResponseDTO.ok("로그아웃 되었습니다.");
    }

    // 인증 코드 전송
    @PostMapping("/send-verification")
    public ResponseDTO<?> sendVerificationEmaiil(@RequestParam String email) {
        userService.checkIfEmailAlreadyUsed(email);
        emailVerificationService.sendVerificationCode(email);
        return ResponseDTO.ok("이메일 인증 코드가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseDTO<?> verifyEmailCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (isVerified) {
            return ResponseDTO.ok(true);
        } else {
            return ResponseDTO.fail(new CommonException(ErrorCode.INVALID_VERIFICATION_CODE));
        }
    }
}
