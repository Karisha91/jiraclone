package com.ivan.jiraclone.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;
    private String message;
    private Long issueId;
    @JsonProperty("isRead")
    private boolean read;

    public NotificationDTO(String message, Long issueId, Long id, boolean isRead) {
        this.message = message;
        this.issueId = issueId;
        this.id = id;
        this.read = isRead;
    }

}
