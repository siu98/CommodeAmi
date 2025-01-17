package com.siuuuuu.commodeami.user.command.application.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
public class EmailVerificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailVerificationService(RedisTemplate<String, String> redisTemplate,
                                    JavaMailSender javaMailSender) {
        this.redisTemplate = redisTemplate;
        this.javaMailSender = javaMailSender;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성
        return String.valueOf(code);
    }

    // 인증 코드 생성 및 Redis에 저장(5분 TTL)
    public String sendVerificationCode(String email) {

        String code = generateVerificationCode();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, code, Duration.ofMinutes(5));

        sendVerificationEmail(email, code);
        return code;
    }

    // 인증 코드 검즌
//    public void sendVerificationEmail(String email, String code) {
//        String subject = "이메일 인증 코드";
//        String message = "인증 코드: " + code + "\n" + "이 코드는 5분 동안 유효합니다.";
//
//        SimpleMailMessage emailMessage = new SimpleMailMessage();
//        emailMessage.setTo(email);
//        emailMessage.setSubject(subject);
//        emailMessage.setText(message);
//        emailMessage.setFrom("parancandy@gmail.com");
//        log.info("code: {}", code);
//        javaMailSender.send(emailMessage);
//    }

    public void sendVerificationEmail(String email, String code) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[Commode Ami] 회원가입 용 인증 코드");

            // HTML 이메일 내용 작성
            String htmlContent = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                    "<h2 style='color: #4CAF50;'>인증 코드: <span style='color: #000;'>" + code + "</span></h2>" +
                    "<p style='font-size: 16px;'>이 코드는 <strong>5분</strong> 동안 유효합니다.</p>" +
                    "<p style='font-size: 14px; color: #555;'>CommodeAmi 팀</p>" +
                    "</div>";

            helper.setText(htmlContent, true); // true로 설정해야 HTML로 처리됩니다.

            javaMailSender.send(mimeMessage);
            log.info("Verification email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to {}", email, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public boolean verifyCode(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedCode = ops.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }
}
