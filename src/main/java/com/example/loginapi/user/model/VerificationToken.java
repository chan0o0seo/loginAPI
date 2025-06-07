package com.example.loginapi.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    private String token;                  // UUID
    @OneToOne(fetch = FetchType.LAZY)
    private Users user;
    private Instant expiryDate;
}