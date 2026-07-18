package com.ivan.jiraclone.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequest {


    @NotBlank(message = "Project name is required")
    @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
    private String projectName;
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    private Long workspaceId;
}
