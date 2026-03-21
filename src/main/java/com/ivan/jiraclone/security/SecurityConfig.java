package com.ivan.jiraclone.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration  // 7# "this is a setup class, read at startup"
public class SecurityConfig {



    @Bean // "create this object and put it in the warehouse"
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
