package com.siuuuuu.commodeami.user.command.application.service;

import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.user.command.aggregate.dto.UserDTO;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import com.siuuuuu.commodeami.user.command.domain.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public AppUserServiceImpl(UserRepository userRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              EmailVerificationService emailVerificationService){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    @Transactional
    public void registUser(UserDTO newUser) {

        // 이미 존재하는지 확인
        User existingUser = userRepository.findUserByEmail(newUser.getEmail());

        if (existingUser != null) {
            throw new CommonException(ErrorCode.EXIST_USER_ID);
        }

        User user = new User();
        user.setUserName(newUser.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setBirthDate(newUser.getBirthDate());
        user.setNickname(newUser.getNickname());
        user.setProfile(newUser.getProfile());
        user.setGender(newUser.getGender());
//        user.setCreatedAt(newUser.getCreatedAt());
        log.info("새로 등록되는 유저정보 registUser: {}", user);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String currentPwd, String newPwd) {

        User exstingUser =
                userRepository.findById(userId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        log.info("비밀번호 변경 시작");
        log.info("입력받은 현재 비밀번호: {}", currentPwd);
        log.info("입력받은 새 비밀번호: {}", newPwd);
        if (!bCryptPasswordEncoder.matches(currentPwd, exstingUser.getPassword())) {
            log.info("현재 비밀번호를 잘못 입력했을 경우");
            throw new CommonException(ErrorCode.INVALID_PASSWORD);
        }

        if (bCryptPasswordEncoder.matches(newPwd, exstingUser.getPassword())) {
            log.info("새 비밀번호가 현재 비밀번호와 깉은 경우");
            throw new CommonException(ErrorCode.EXIST_PASSWORD);
        }

        newPwd = bCryptPasswordEncoder.encode(newPwd);
        exstingUser.setPassword(newPwd);
        userRepository.save(exstingUser);
    }

    @Override
    @Transactional
    public void generateAndSendTemporaryPassword(String email) {
        User user = userRepository.findByEmail(email);
//                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 임시 비밀번호 생성
        String tempPassword = emailVerificationService.generateTemporaryPassword();

        // 사용자 비밀번호 업데이트
        String encodedPassword = bCryptPasswordEncoder.encode(tempPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // 이메일로 임시 비밀번호 전송
        emailVerificationService.sendTemporaryPassword(email, tempPassword);

        log.info("임시 비밀번호가 생성되고 이메일로 전송되었습니다: {}", email);
    }

    @Override
    @Transactional
    public void updateLastAccessTime(String email) {
        User existingUser = userRepository.findUserByEmail(email);

        if (existingUser == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

//        existingUser.setLastAccessTime(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(existingUser);
    }

    @Override
    public boolean checkIfEmailAlreadyUsed(String email) {
        User foundUser = userRepository.findByEmail(email);
        if (foundUser != null) {
            throw new CommonException(ErrorCode.DUPLICATE_EMAIL_EXISTS);
        }
        return false;
    }

}
