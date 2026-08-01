package com.ivan.jiraclone.dto;


import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String newPassword;
    private String oldPassword;


}
