package com.ivan.jiraclone.dto;

import com.ivan.jiraclone.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoveIssueRequest {
    @NotNull
    private Status status;
    @NotNull
    private Double position;
}