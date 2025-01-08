package com.siuuuuu.commodeami.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.user.command.aggregate.entity.CustomUser;
import com.siuuuuu.commodeami.user.query.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/* 설명. OncePerRequestFilter를 상속받아 doFilterInternal을 오버라이딩 한다.(한번만 실행되는 필터) */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_RT_PREFIX = "RT:";
    private static final String REDIS_BL_PREFIX = "BL:";

    public JwtFilter(UserService userService, JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
       this.userService = userService;
       this.jwtUtil = jwtUtil;
       this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path =request.getRequestURI();
        log.info("현재 요청 path {}: " + path);
        return path.startsWith("/api/user/login")
                || path.startsWith("/api/user/regist")
                || path.startsWith("/api/movie")

        // swagger 관련 모든 경로 추후 추가
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/docs/login")
                || path.startsWith("/sse/subscribe")
                || path.startsWith("/swagger-custom-ui.html")
                || path.startsWith("/ws");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("JWT 인증 필터 시작");
            String accessToken = extractAccessToken(request);

            if (accessToken != null) {
                processAccessToken(accessToken, request, response);
            } else {
                processRefreshToken(request, response);
            }

            filterChain.doFilter(request, response);
        } catch (CommonException e) {
            handleAuthenticationException(response, e);
        }
    }

    private void processAccessToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {
        if (jwtUtil.isTokenExpired(accessToken)) {
            log.info("만료된 accessToken 감지, 재발급 프로세스 시작");
            handleExpiredAccessToken(accessToken, request, response);
            return;
        }

        validateAccessToken(accessToken);
        authenticateWithToken(accessToken);
    }

    private void handleExpiredAccessToken (String expiredToken, HttpServletRequest request, HttpServletResponse response) {

        // 1. 만료된 토큰 블랙리스트 처리
        addToBlacklist(expiredToken);

        // 2. RefreshToken으로 새로운 AccessToken 발급
        String refreshToken = extractRefreshTokenFromCookie(request);
        String userEmail = jwtUtil.getEmailFromToken(refreshToken);

        // 3. RefreshToken 유효성 검증
        validateRefreshToken(refreshToken, userEmail);

        // 4. 새로운 AccessToken 발급 및 설정
        String newAccessToken = jwtUtil.generateAccessToken(userEmail);
        response.addHeader("Authorization", "Bearer " + newAccessToken);

        // 5. 새로운 AccessToken으로 인증 처라
        authenticateWithToken(newAccessToken);
    }

    private void addToBlacklist(String token) {
        String userEmail = jwtUtil.getEmailFromToken(token);
        Long remainingTime = jwtUtil.getRemainingTime(token);
        redisTemplate.opsForValue().set(
                REDIS_BL_PREFIX + token,
                userEmail,
                remainingTime,
                TimeUnit.MICROSECONDS
        );
    }

    private void validateAccessToken(String token) {
        // 블랙리스트 체크
        String blacklisted = redisTemplate.opsForValue().get(REDIS_BL_PREFIX + token);
        if (blacklisted != null) {
            log.info("블랙리스트에 등록된 토큰 감지");
            throw new CommonException(ErrorCode.LOGOUT_ACCESS_TOKEN);
        }
    }

    private void authenticateWithToken(String token) {
        Claims claims = jwtUtil.parseClaims(token);
        Authentication authentication = getAuthentication(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getAuthentication(Claims claims) {
        String email = claims.getSubject();
        List<String> roles = claims.get("auth", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        CustomUser user = (CustomUser) userService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    private void processRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        String userEmail = jwtUtil.getEmailFromToken(refreshToken);

        validateRefreshToken(refreshToken, userEmail);

        TokenPair tokenPair = handleTokenIssue(refreshToken, userEmail);
        setAuthenticationResponse(response, tokenPair);
        authenticateWithToken(tokenPair.accessToken());
    }

    private TokenPair handleTokenIssue(String refreshToken, String userEmail) {
        String newAccessToken = jwtUtil.generateAccessToken(userEmail);

        return new TokenPair(newAccessToken, refreshToken);
    }

    private void validateRefreshToken(String refreshToken, String userEmail) {
        String storedToken = redisTemplate.opsForValue().get(REDIS_RT_PREFIX + userEmail);

        if (storedToken == null) {
            log.info("저장된 RT 없음");
            throw new CommonException(ErrorCode.EXPIRED_TOKEN_ERROR);
        }

        if (!storedToken.equals(refreshToken)) {
            log.info("RT 불일치");
            redisTemplate.delete(REDIS_RT_PREFIX + userEmail);
            throw new CommonException(ErrorCode.ACCESS_DENIED);
        }

    }

    private void setAuthenticationResponse(HttpServletResponse response, TokenPair tokenPair) {
        response.addHeader("Authorization", "Bearer " + tokenPair.accessToken());
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        log.info("Cookie에서 Rt 추출하는 method start");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            log.info("refreshToken을 담은 쿠키 목록이 없 을 경우 예외");
            throw new CommonException(ErrorCode.NOT_FOUND_COOKIE);
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                log.info("refreshToken을 담은 쿠키 도착");
                log.info("쿠키 이름: {}", cookie.getName());
                log.info("쿠키 값: {}", cookie.getValue());
                log.info("쿠키 maxAge: {}", cookie.getMaxAge());
            }
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if (refreshToken == null) {
            log.info("cookie 목록에서 refreshToken이 없는 경우 예외 로그");
            throw new CommonException(ErrorCode.NOT_FOUND_COOKIE);
        }

        return refreshToken;
    }

    private void handleAuthenticationException(HttpServletResponse response, CommonException e) throws IOException {
        response.setStatus(e.getErrorCode().getHttpStatus().value());
        response.setContentType("application/json;charset=utf-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(),
                Map.of("error", e.getErrorCode().getMessage()));
    }

    private record TokenPair (String accessToken, String refreshToken) {}

//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    public JwtFilter(UserService userService, JwtUtil jwtUtil) {
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    /* 설명. 들고 온(Request Header) 토큰이 유효한지 판별 및 인증(Authentication 객체로 관리) */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        log.info("UsernamePasswordAuthenticationFilter보다 먼저 동작하는 필터");
//
//        String authorizationHeader = request.getHeader("Authorization");
//        log.info("Authorization header: {}", authorizationHeader);
//
//        /* 설명. JWT 토큰이 Request Header에 있는 경우(로그인 이후 요청일 경우) */
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            log.info("토큰 : " + token);
//            if(jwtUtil.validateToken(token)) {
//                Authentication authentication = jwtUtil.getAuthentication(token);
//                log.info("JWTFilter를 통과한 유효한 토큰을 통해 security가 관리할 principal 객체: {}", authentication);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        /* 설명. 위의 if문으로 인증된 Authentication 객체가 principal 객체로 관리되지 않는다면 다음 필터 실행 */
//        filterChain.doFilter(request, response);
//    }

}
