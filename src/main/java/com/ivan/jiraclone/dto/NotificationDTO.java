package com.ivan.jiraclone.dto;


import lombok.Data;

@Data
public class NotificationDTO {

    private String message;
    private Long issueId;

    public NotificationDTO(String message, Long issueId) {
        this.message = message;
        this.issueId = issueId;
    }

}
