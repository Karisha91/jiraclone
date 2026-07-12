package com.ivan.jiraclone.dto;

import com.ivan.jiraclone.enums.Priority;
import com.ivan.jiraclone.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateIssueRequest {


    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Status status;

    @NotNull
    private Priority priority;

    @NotNull
    private Long projectId;

    @NotNull
    private Long reporterId;

}
