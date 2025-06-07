package com.example.loginapi.user.service;

import com.example.loginapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(users -> {
                    String[] rolesArray = users.getRoles().split(",");
                    return User.builder()
                            .username(users.getEmail())
                            .password(users.getPassword())
                            .roles(rolesArray)
                            .build();
                })
                .orElseThrow(() ->
                        new UsernameNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다: " + username)
                );
    }
}
