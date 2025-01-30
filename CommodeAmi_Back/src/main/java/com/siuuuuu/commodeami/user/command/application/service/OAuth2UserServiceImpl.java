//package com.siuuuuu.commodeami.user.command.application.service;
//
//import com.siuuuuu.commodeami.common.exception.CommonException;
//import com.siuuuuu.commodeami.common.exception.ErrorCode;
//import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
//import com.siuuuuu.commodeami.user.command.aggregate.vo.LoginRequestVO;
//import com.siuuuuu.commodeami.user.command.aggregate.vo.OAuth2LoginVO;
//import com.siuuuuu.commodeami.user.command.domain.repository.UserRepository;
//import com.siuuuuu.commodeami.user.security.JwtUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class OAuth2UserServiceImpl implements OAuth2UserService {
//
//    private final AppUserService appUserService;
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final RestTemplate restTemplate;
//    private ModelMapper modelMapper;
//
//    @Autowired
//    public OAuth2UserServiceImpl(AppUserService appUserService,
//                                 UserRepository userRepository,
//                                 JwtUtil jwtUtil,
//                                 RestTemplate restTemplate,
//                                 ModelMapper modelMapper) {
//        this.appUserService = appUserService;
//        this.userRepository = userRepository;
//        this.jwtUtil = jwtUtil;
//        this.restTemplate = restTemplate;
//        this.modelMapper = modelMapper;
//    }
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
//
//        User loginUser = userRepository.findByUserIdentifier(userIdentifier);
//        if (loginUser == null) {
//            throw new CommonException(ErrorCode.NOT_FOUND_USER);
//        }
//
//        String encryptedPwd = loginUser.getEncryptedPassword();
//        if (encryptedPwd == null) {
//            encryptedPwd = "{noop}";
//        }
//
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
//
//        return new User(loginUser.getUserIdentifier()
//                , encryptedPwd
//                , true
//                , true
//                , true
//                , true
//                , grantedAuthorities);
//
//    }
//
//    @Override
//    public OAuth2LoginVO processKakaoUser(String code) {
//        log.info("OAuth2UserService - code: {}", code);
//
//        String accessToken = getKakaoAccessToken(code);
//        Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
//
//        String kakaoId = String.valueOf(userInfo.get("id"));
//        String email = (String) userInfo.get("email");
//        String name = (String) userInfo.get("nickname");
//        log.info("kakaoId: {}", kakaoId);
//        log.info("email: {}", email);
//        log.info("name: {}", name);
//        if (email == null || email.isEmpty()) {
//            email = kakaoId + "@kakao.com";
//        }
//
//        User user = userRepository.findByUserIdentifier("KAKAO_" + email);
//
//        if (user == null) {
//            log.info("regist new Kakao User: {}", name);
//
//            LoginRequestVO newUser = new LoginRequestVO();
//            newUser.setEmail(email);
////            newUser.setUsername(name != null ? name : "KakaoUser");
////            newUser.setUserAuthId(kakaoId);
//            newUser.setPassword(UUID.randomUUID().toString());
////            newUser.setSignupPath(SignupPath.KAKAO);
////            newUser.setNickname("Kakao@"+kakaoId);
//            log.info("regist newUser: {}", newUser);
//            appUserService.registUser(newUser);
//
//            user = userRepository.findByUserIdentifier("KAKAO_" + email);
//
//        }
//        OAuth2LoginVO user2 = modelMapper.map(user, OAuth2LoginVO.class);
//
//        user.setAccessToken(accessToken);
//
//        log.info("userEntity: {}", user);
//        String refreshToken = jwtUtil.generateRefreshToken(user, new ArrayList<>());
//        log.info("refreshToken: {}", refreshToken);
//        user.setRefreshToken(refreshToken);
//
//        return user2;
//
//
//}
