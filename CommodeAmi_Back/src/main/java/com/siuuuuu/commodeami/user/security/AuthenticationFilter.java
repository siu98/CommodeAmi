package com.siuuuuu.commodeami.user.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.user.command.aggregate.entity.CustomUser;
import com.siuuuuu.commodeami.user.command.aggregate.vo.LoginRequestVO;
import com.siuuuuu.commodeami.user.command.application.service.AppUserService;
import com.siuuuuu.commodeami.user.query.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final AppUserService appUserService;
    private final Environment env;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                AppUserService appUserService,
                                Environment env,
                                RedisTemplate<String, String> redisTemplate) {
        super(authenticationManager);
        this.userService = userService;
        this.appUserService = appUserService;
        this.env = env;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                               HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 기반 인증 시작");
        log.info("attemptAuthentication method request LocalPort: {}", request.getLocalPort());
        try {
            LoginRequestVO creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestVO.class);
            log.info("attemptAuthentication method creds객체 정보 : {}", creds);

            CustomUser user = (CustomUser) userService.loadUserByUsername(creds.getEmail());

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, creds.getPassword(), new ArrayList<>());
            log.info("attemptAuthentication authToken(성공 후 생성된 인증 토큰): {}", authToken);

            // AuthenticationManager에 전달
            Authentication auth = getAuthenticationManager().authenticate(authToken);
            log.info("Authentication result: {}", auth);  // 이 로그가 찍히는지 확인

            return auth;
        } catch (Exception e) {
            throw new AuthenticationServiceException("유저 인증 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain chain,
                                           Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication! Principal 객체 : {}", authResult);

        // 고유 UserType으로 다운 캐스팅
        CustomUser customUser = (CustomUser) authResult.getPrincipal();

        // accessToken에는 회원아이디, 회원 번호, 이름, 프로필 사진
        String email = customUser.getUsername();
        log.info("email: {}", email);

        Claims accessClaims = Jwts.claims().setSubject(email);
        accessClaims.put("userName", customUser.getUserName());
        accessClaims.put ("userid", customUser.getUserId());
        accessClaims.put("profile", customUser.getProfile());
        accessClaims.put("nickname", customUser.getNickName());

        // refreshToken에는 아이디만 넣기
        Claims refreshClaims = Jwts.claims().setSubject(email);
        List<String> roles = authResult.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());
        accessClaims.put("auth", roles);

        // 토큰 만료 시간 설정
        long accessExpiration =
                System.currentTimeMillis() + getExpirationTime(env.getProperty("token.access-expiration-time"));
        long refreshExpiration =
                System.currentTimeMillis() + getExpirationTime(env.getProperty("token.refresh-expiration-time"));

        // AccessToken 생성
        String accessToken = Jwts.builder()
                .setClaims(accessClaims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        // RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setClaims(refreshClaims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        // Ex ) Key: RT:abc@gmail.com, Value: RT정보, TTL(Data 만료시간)
        redisTemplate.opsForValue().set(
                "RT:" + email,
                refreshToken,
                refreshExpiration,
                TimeUnit.MICROSECONDS
        );

        // RefreshToken은 HttpOnly Cookie에 담기
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api")
                .build();

        // AccessToken 헤더에 담기
        response.addHeader("Authorization", "Bearer " + accessToken);
        // Cookie를 헤더에 담아 전송
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 마지막 로그인 시간 업데이트
        appUserService.updateLastAccessTime(email);

        ResponseDTO<String> responseDTO = ResponseDTO.ok("로그인에 성공했습니다.");

        // JSON 문자열로 반환
        String JSON = new ObjectMapper().writeValueAsString(responseDTO);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON);
    }

    private long getExpirationTime(String expirationTime) {
        if (expirationTime == null) {

            return 3600000;
        }
        return Long.parseLong(expirationTime);
    }



    //
//    private UserService userService;
//    private Environment env;
//
//    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
//        super(authenticationManager);
//        this.userService = userService;
//        this.env = env;
//    }
//
//    /* 설명. 로그인 시도 시 동작하는 기능(POST /login 요청 시) */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        /* 설명. request body에 담긴 내용을 우리가 만든 RequestLoginVO 타입에 담는다.(일종의 @RequestBody의 개념) */
//        try {
//            RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);
//
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /* 설명. 로그인 성공 시 JWT 토큰 생성하는 기능 */
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//
//        log.info("로그인 성공하고 security가 관리하는 principal객체(authResult): {}", authResult);
//
//        /* 설명. 로그인 이후 관리되고 있는 Authentication 객체를 활용해 JWT Token 만들기 */
//        log.info("시크릿 키: " + env.getProperty("token.secret"));
//
//        /* 설명. 토큰의 payload에 어떤 값을 담고 싶은지에 따라 고민해서 재료를 수집한다.(id, 가진 권한들, 만료시간) */
//        String userName = ((User)authResult.getPrincipal()).getEmail();  // id의 개념(우리는 email로 작성했음)
//        log.info("인증된 회원의 id: " + userName);
//
//        /* 설명. 권한들을 꺼내 List<String>로 변환 */
//        List<String> roles = authResult.getAuthorities().stream()
//                .map(role -> role.getAuthority())
//                .collect(Collectors.toList());
//
//        /* 설명. 재료들로 토큰 만들기(Jwt Token 라이브러리 추가(3가지)하기) */
//        Claims claims = Jwts.claims().setSubject(userName);
//        claims.put("auth", roles);      // 비공개 클레임은 Claims에서 제공하는 put을 활용해 추가한다.
//
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(new Date(System.currentTimeMillis()
//                        + Long.parseLong(env.getProperty("token.expiration_time"))))
//                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
//                .compact();
//
//        response.addHeader("token", token);
//    }
}
