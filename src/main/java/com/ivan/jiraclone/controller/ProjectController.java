
package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.service.ProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;

    }
    @GetMapping("/{id}")
    public ProjectDTO getProjectDTOById(@PathVariable long id) {
        return projectService.convertToDTO(projectService.getProjectById(id));
    }

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProjectDTO createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }


}
