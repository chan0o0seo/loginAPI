package com.example.loginapi.user.service;

import com.example.loginapi.user.model.VerificationToken;
import com.example.loginapi.user.model.dto.SignupRequest;
import com.example.loginapi.user.model.Users;
import com.example.loginapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    /**
     * 회원가입 처리
     */
    @Transactional
    public void registerUser(SignupRequest req) {
        if (userRepository.existsById(req.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Users user = Users.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles("USER")
                .build();

        userRepository.save(user);

        VerificationToken vt = verificationTokenService.createToken(user);

        emailService.sendVerificationEmailAsync(user, vt.getToken());

    }
}