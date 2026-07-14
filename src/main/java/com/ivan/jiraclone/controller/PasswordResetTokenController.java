package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.ForgotPasswordRequest;
import com.ivan.jiraclone.dto.ResetPasswordRequest;
import com.ivan.jiraclone.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetTokenController {


    private final PasswordResetService  passwordResetService;

    public PasswordResetTokenController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody ForgotPasswordRequest  forgotPasswordRequest) {
        passwordResetService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Password reset email has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        System.out.println("Token received: " + resetPasswordRequest.getToken());
        passwordResetService.resetPassword(resetPasswordRequest.getNewPassword(),  resetPasswordRequest.getToken());
        return ResponseEntity.ok("Password has been successfully reset");
    }

}
