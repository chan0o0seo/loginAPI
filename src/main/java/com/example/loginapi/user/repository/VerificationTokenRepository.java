package com.example.loginapi.user.repository;

import com.example.loginapi.user.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
}
