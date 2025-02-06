package com.siuuuuu.commodeami.user.security;

import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.user.command.aggregate.entity.CustomUser;
import com.siuuuuu.commodeami.user.query.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final UserService userService;

    @Autowired
    public JwtUtil(@Value("${token.secret}") String secretKey,
                   @Value("${token.access-expiration-time}") long accessExpiration,
                   @Value("${token.refresh-expiration-time}") long refreshExpiration,
                    UserService userService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.userService = userService;
    }

    // accessToken으로부터 Claims 추출
    public Claims parseClaims(String accessToken) {
        log.info("Claim을 추출하기 위한 accessToken: {}", accessToken);
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        return claims;
    }

    // refreshToken으로부터 Email 추출
    public String getEmailFromToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken).getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new CommonException(ErrorCode.EXPIRED_TOKEN_ERROR);
        }
    }

    // AccessToken 생성 로직
    public String generateAccessToken(String userEmail) {

        CustomUser user = (CustomUser) userService.loadUserByUsername(userEmail);

        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("userName", user.getUserName());
        claims.put("userId", user.getUserId());
        claims.put("profile", user.getProfile());
        claims.put("nickName", user.getNickName());

        claims.put("auth", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        log.info("Claims before token generation: " + claims);

        return buildToken(claims, accessExpiration);
    }

    // Token 생성 공통 로직
    public String buildToken(Claims claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseClaims(token);

        return claims.getExpiration().before(new Date());
    }

    public Long getRemainingTime(String token) {
        Claims claims = parseClaims(token);

        return claims.getExpiration().getTime() - new Date().getTime();
    }

}
