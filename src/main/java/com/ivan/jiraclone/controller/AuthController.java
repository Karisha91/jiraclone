package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.AuthRequest;
import com.ivan.jiraclone.dto.AuthResponse;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.security.JwtUtil;
import com.ivan.jiraclone.service.RateLimitService;
import com.ivan.jiraclone.service.UserService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RateLimitService rateLimitService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService,
                          BCryptPasswordEncoder passwordEncoder, RateLimitService rateLimitService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.rateLimitService = rateLimitService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpServletRequest request) {
        String clientIP = request.getHeader("X-Forwarded-For");

        if (clientIP == null || clientIP.isEmpty()) {
            clientIP =request.getRemoteAddr();
        }
        Bucket bucket = rateLimitService.resolveBucket(clientIP);
        if (bucket.tryConsume(1)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.ok(userService.createUser(user));
        }  else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many register attempts. Please try again later.");
        }


    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        String clientIP = httpRequest.getHeader("X-Forwarded-For");

        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = httpRequest.getRemoteAddr();
        }
        Bucket bucket = rateLimitService.resolveBucket(clientIP);
        if (bucket.tryConsume(1)) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = userService.findByUsername(request.getUsername());
            String token = jwtUtil.generateToken(request.getUsername(), user.getId(),user.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many login attempts. Please try again later.");
        }

    }
}
