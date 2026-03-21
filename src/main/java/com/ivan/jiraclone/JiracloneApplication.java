package com.ivan.jiraclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class JiracloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiracloneApplication.class, args);
	}

}