package com.ivan.jiraclone.dto;


import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class AuthRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;

}
