package com.ivan.jiraclone.dto;

import com.ivan.jiraclone.enums.Priority;
import com.ivan.jiraclone.enums.Status;
import lombok.Data;


@Data
public class IssueDTO {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String projectName;
    private String reporterUsername;
    private String assigneeUsername;

}
