package com.example.loginapi.user.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

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

    private boolean enabled = false;
}
