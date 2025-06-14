package com.example.loginapi.user.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Users {

    @Id
    private String email;

    private String password;

    private String roles;

    private String type;

    private boolean enabled = false;

    private LocalDateTime deletedAt;
}
