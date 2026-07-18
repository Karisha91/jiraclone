package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.PasswordResetTokenRepository;
import com.ivan.jiraclone.exception.BadRequestException;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.model.PasswordResetToken;
import com.ivan.jiraclone.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private PasswordResetTokenRepository passwordResetTokenRepository;
    private UserService userService;
    private JavaMailSender javaMailSender;
    private PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository, UserService userService, JavaMailSender javaMailSender,  PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgotPassword(String email) {
        User user = userService.findByEmail(email);

        if (user == null) {
            return;
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        passwordResetToken.setUsed(false);
        passwordResetTokenRepository.save(passwordResetToken);
        sendResetEmail(email, token);


    }

    public void sendResetEmail(String email, String token) {


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset Password Link");
        mailMessage.setText("Click here to reset your password: http://localhost:5173/reset-password?token=" + token);
        javaMailSender.send(mailMessage);

    }

    public void resetPassword(String newPassword, String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
        User user = passwordResetToken.getUser();
        if (passwordResetToken.isUsed() || passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token is used or is expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);
        userService.saveUser(user);

    }
}