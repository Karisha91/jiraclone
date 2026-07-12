package com.ivan.jiraclone.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCommentRequest {

    @NotBlank
    private String content;
    private Long issueId;
}
