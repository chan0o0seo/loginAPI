package com.example.loginapi.user.service;

import com.example.loginapi.user.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${app.frontend.url}") private String frontendUrl;

    public void sendVerificationEmail(Users user, String token) {
        String link = frontendUrl + "/api/auth/verify-email?token=" + token;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("이메일 인증 안내");
        mail.setText(
                "안녕하세요!\n\n" +
                        "다음 링크를 클릭하여 이메일 인증을 완료하세요:\n" +
                        link + "\n\n" +
                        "감사합니다."
        );
        mailSender.send(mail);
    }

    @Async
    public void sendVerificationEmailAsync(Users user, String token) {
        sendVerificationEmail(user, token);
    }
}