package com.siuuuuu.commodeami.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.user.command.application.service.AppUserService;
import com.siuuuuu.commodeami.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserService appUserService;
    private final UserService userService;
    private final Environment env;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // 의존성 주입
    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder,
                       AppUserService appUserService,
                       UserService userService,
                       Environment env,
                       RedisTemplate<String, String> redisTemplate,
                       JwtUtil jwtUtil) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.appUserService = appUserService;
        this.userService = userService;
        this.env = env;
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configure(http))
                .csrf((csrf) -> csrf.disable());

        // 로그인 시, http에 추가될 authenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // userDetails 클래스를 물려받은 우리 서비스 고유의 userService인식 + 암호화 방식 인식
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // 회원가입만 열기
        http.authorizeHttpRequests((auth) ->
                auth.requestMatchers(new AntPathRequestMatcher("/api/user/regist")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user/send-verification")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user/reset")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user/verify-code")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/movie/**")).permitAll() // 이 경로는 인증 없이 접근 가능
                        .requestMatchers(new AntPathRequestMatcher("/api/actor/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/average-scope/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/recommendation")).permitAll()
                // Swagger UI 경로 후에 추가 예정
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-custom-ui.html")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/docs/login")).permitAll()
                        .requestMatchers("/auth/**", "/ws/**").permitAll()
                        .anyRequest().authenticated())

                // manager 등록
                .authenticationManager(authenticationManager)
                // session 방식 사용 X
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JWT filter 전에 로그아웃 필터 끼기
                .logout(logout -> logout
                        .logoutUrl("/api/user/logout")
                        .addLogoutHandler(new CustomLogoutHandler(redisTemplate, jwtUtil))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            new ObjectMapper().writeValue(response.getOutputStream(),
                                    ResponseDTO.ok("로그아웃이 성공적으로 되었습니다."));
                        })
                )
                .addFilter(getAuthenticationFilter(authenticationManager))
                .addFilterBefore(new JwtFilter(userService, jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인증 method, filter를 반환하는 메서드
    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager, userService, appUserService, env, redisTemplate);
        authenticationFilter.setFilterProcessesUrl("/api/user/login");

        return authenticationFilter;
    }

}
