package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.*;
import com.ivan.jiraclone.model.Workspace;
import com.ivan.jiraclone.service.ProjectService;
import com.ivan.jiraclone.service.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final ProjectService projectService;


    public WorkspaceController(WorkspaceService workspaceService ,ProjectService projectService ) {
        this.workspaceService = workspaceService;
        this.projectService = projectService;
    }


    @GetMapping
    public List<WorkspaceResponse> getWorkspaces(Principal principal) {
        return workspaceService.getWorkspaces(principal);

    }

    @GetMapping("/{id}")
    public WorkspaceResponse getWorkspace(@PathVariable long id, Principal principal) {
        return workspaceService.getWorkspace(id);

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping
    public WorkspaceResponse createWorkspace(@Valid @RequestBody WorkspaceRequest workspaceRequest, Principal principal) {
        return workspaceService.createWorkspace(workspaceRequest, principal);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PutMapping("/{id}")
    public WorkspaceResponse  updateWorkspace(@PathVariable long id, @Valid @RequestBody WorkspaceRequest workspaceRequest, Principal principal) {
        return workspaceService.updateWorkspace(id, workspaceRequest, principal);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/{id}")
    public void  deleteWorkspace(@PathVariable long id, Principal principal) {
        workspaceService.deleteWorkspace(id, principal);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/{id}/members")
    public MemberSummary addMemberToWorkspace(@PathVariable long id, @Valid @RequestBody UserDTO userDTO, Principal principal ) {
       return workspaceService.addMemberToWorkspace(id, userDTO, principal);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/{id}/members")
    public void removeMemberFromWorkspace(@PathVariable long id, @Valid @RequestBody UserDTO userDTO, Principal principal) {
        workspaceService.removeMemberFromWorkspace(id, userDTO, principal);

    }
    @GetMapping("/{workspaceId}/projects")
    public List<ProjectDTO> getAllProjects(@PathVariable long workspaceId) {
        return projectService.getAllProjectsByWorkspaceId(workspaceId);
    }







}
