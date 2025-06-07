package com.example.loginapi.user.service;


import com.example.loginapi.user.model.Users;
import com.example.loginapi.user.model.VerificationToken;
import com.example.loginapi.user.repository.UserRepository;
import com.example.loginapi.user.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken createToken(Users user) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(1, ChronoUnit.DAYS);

        VerificationToken vt = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiry)
                .build();
        return verificationTokenRepository.save(vt);
    }

    public Optional<Users> validateToken(String token) {
        return verificationTokenRepository.findById(token)
                .filter(vt -> vt.getExpiryDate().isAfter(Instant.now()))
                .map(VerificationToken::getUser);
    }
}
