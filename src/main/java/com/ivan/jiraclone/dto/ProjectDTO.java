package com.ivan.jiraclone.dto;


import lombok.Data;

@Data
public class ProjectDTO {

    private Long id;

    private String projectName;

    private String description;

    private String owner;


}
