package com.ivan.jiraclone.dto;



import lombok.Data;

import java.util.List;

@Data
public class ProjectDTO {

    private Long id;


    private String projectName;

    private String description;

    private Long workspaceId;

    private List<IssueDTO> issues;


}
