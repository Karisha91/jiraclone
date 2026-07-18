package com.ivan.jiraclone.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WorkspaceRequest {


    @NotBlank
    private String name;
    @NotBlank
    private String description;

}
