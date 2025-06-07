package com.example.loginapi.user.repository;

import com.example.loginapi.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {
}
