package com.ivan.jiraclone.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class WorkspaceResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private OwnerSummary owner;
    private List<MemberSummary> members;
    private List<ProjectSummary> projects;

}
