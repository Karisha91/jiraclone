
package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.CreateProjectRequest;
import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;

    }
    @GetMapping("/{projectId}")
    public ProjectDTO getProjectDTOById(@PathVariable long projectId) {
        return projectService.convertToDTO(projectService.getProjectById(projectId));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProjectById(@PathVariable Long id, Principal principal) {
        projectService.deleteProject(id, principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProjectDTO createProject(@Valid @RequestBody CreateProjectRequest request, Principal principal) {
        return projectService.createProject(request, principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProjectDTO updateProject(@PathVariable Long id, @Valid @RequestBody CreateProjectRequest request, Principal principal) {
        return projectService.updateProject(id, request, principal);
    }


}
