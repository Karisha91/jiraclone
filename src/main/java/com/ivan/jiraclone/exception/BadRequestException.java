package com.ivan.jiraclone.exception;

public class BadRequestException  extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
