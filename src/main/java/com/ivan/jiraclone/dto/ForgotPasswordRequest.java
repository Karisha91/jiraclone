package com.ivan.jiraclone.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {


    @NotBlank
    private String email;

}
