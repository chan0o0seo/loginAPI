package com.example.loginapi.user.service;

import com.example.loginapi.user.model.VerificationToken;
import com.example.loginapi.user.model.dto.SignupRequest;
import com.example.loginapi.user.model.Users;
import com.example.loginapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

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
                .type("local")
                .roles("USER")
                .build();

        userRepository.save(user);

        VerificationToken vt = verificationTokenService.createToken(user);

        emailService.sendVerificationEmailAsync(user, vt.getToken());

    }

    /**
     *  Google 회원가입 처리
     */
    @Transactional
    public Users registerUser(String registrationId,OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();

        System.out.println(registrationId);
        String email = (String) attributes.get("email");

        System.out.println(email);
        if(userRepository.existsById(email)) {
            return userRepository.findById(email).get();
        }
        String randomPwd = UUID.randomUUID().toString();
        Users user = Users.builder()
                .email(email)
                .password(randomPwd)
                .type(registrationId)
                .enabled(true)
                .roles("USER").build();
        userRepository.save(user);
        return user;
    }
}