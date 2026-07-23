package com.ivan.jiraclone.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;
    private String message;
    private Long issueId;
    private Long workspaceId;

    @JsonProperty("isRead")
    private boolean read;

    public NotificationDTO(Long id, String message, Long workspaceId, Long issueId, boolean read) {
        this.id = id;
        this.message = message;
        this.workspaceId = workspaceId;
        this.issueId = issueId;
        this.read = read;
    }
}
