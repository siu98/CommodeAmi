//package com.siuuuuu.commodeami.user.query.service;
//
//import com.siuuuuu.commodeami.user.command.aggregate.dto.UserDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@SpringBootTest
//class UserServiceImplTests {
//
//    @Autowired
//    private UserService userService;
//
//    @DisplayName("프로필 조회")
//    @Test
//    void findProfile() {
//        // given
//        String email = "parancandy@example.com";
//
//        // when
//        UserDTO userDTO = userService.findByEmail(email);
//
//        // then
//        assertNotNull(userDTO, "사용자 정보가 null이 아닙니다.");
//        assertEquals(email, userDTO.getEmail(), "이메일이 일치해야 합니다.");
//
//        log.info("조회된 사용자 프로필: {}", userDTO);
//
//    }
//
//}