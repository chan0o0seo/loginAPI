package com.example.loginapi.user.controller;

import com.example.loginapi.config.jwt.JwtTokenProvider;
import com.example.loginapi.user.model.dto.LoginRequest;
import com.example.loginapi.user.model.dto.SignupRequest;
import com.example.loginapi.user.repository.UserRepository;
import com.example.loginapi.user.service.UserService;
import com.example.loginapi.user.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        Authentication auth = authManager.authenticate(authToken);

        String accessToken = tokenProvider.createAccessToken(auth);
        String refreshToken = tokenProvider.createRefreshToken(auth);
        ResponseCookie cookie = ResponseCookie.from("JWT", accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofMillis(accessExpiration))
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("ok");
    }
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest req) {
        userService.registerUser(req);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return verificationTokenService.validateToken(token)
                .map(user -> {
                    user.setEnabled(true);
                    userRepository.save(user);
                    return ResponseEntity.ok("이메일 인증 성공! 이제 로그인 가능합니다.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 토큰입니다."));
    }
}