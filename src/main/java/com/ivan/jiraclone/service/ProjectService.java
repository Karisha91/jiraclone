package com.ivan.jiraclone.service;
import com.ivan.jiraclone.enums.AuditAction;


import com.ivan.jiraclone.Repository.ProjectRepository;
import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.model.Project;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService) {
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> dtos = new ArrayList<>();
        for (Project project : projects) {
            dtos.add(convertToDTO(project));
        }
        return dtos;
    }

    public ProjectDTO createProject(Project project, Principal principal) {
        Project saved = projectRepository.save(project);
        auditLogService.logAction(principal.getName(), AuditAction.PROJECT_CREATED, "Project", saved.getId(), saved.getName());
        return convertToDTO(saved);
    }

    public Project updateProject(Long id, Project project,Principal principal) {
        Project existingProject = getProjectById(id);
        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());
        auditLogService.logAction(principal.getName(), AuditAction.PROJECT_UPDATED, "Project", existingProject.getId(), existingProject.getName());
        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long id, Principal principal) {
        Project existingProject = getProjectById(id);
        auditLogService.logAction(principal.getName(), AuditAction.PROJECT_DELETED, "Project", id, existingProject.getName());
        projectRepository.deleteById(id);

    }

    public ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setOwner(project.getOwner() != null ? project.getOwner().getUsername() : null);
        return projectDTO;
    }
}
