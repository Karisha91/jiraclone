package com.ivan.jiraclone.dto;


import com.ivan.jiraclone.enums.Priority;
import com.ivan.jiraclone.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateIssueRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Status  status;
    @NotNull
    private Priority priority;


}
