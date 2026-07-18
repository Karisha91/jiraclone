package com.ivan.jiraclone.service;
import com.ivan.jiraclone.dto.CreateProjectRequest;
import com.ivan.jiraclone.dto.WorkspaceResponse;
import com.ivan.jiraclone.enums.AuditAction;


import com.ivan.jiraclone.Repository.ProjectRepository;
import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.Workspace;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;
    private final WorkspaceService workspaceService;

    public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService,WorkspaceService workspaceService) {
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
        this.workspaceService = workspaceService;
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public List<ProjectDTO> getAllProjectsByWorkspaceId(Long workspaceId) {
        List<Project> projects = projectRepository.findByWorkspaceId(workspaceId);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProjectDTO createProject(CreateProjectRequest request, Principal principal) {
        Project project = new Project();
        Workspace workspace = workspaceService.getRealWorkspace(request.getWorkspaceId());
        project.setName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setWorkspace(workspace);
        Project saved = projectRepository.save(project);
        auditLogService.logAction(principal.getName(), AuditAction.PROJECT_CREATED, "Project", saved.getId(), saved.getName());
        return convertToDTO(saved);
    }

    public ProjectDTO updateProject(Long id, CreateProjectRequest request, Principal principal) {
        Project existingProject = getProjectById(id);
        existingProject.setName(request.getProjectName());
        existingProject.setDescription(request.getDescription());
        Project updated = projectRepository.save(existingProject);
        auditLogService.logAction(principal.getName(), AuditAction.PROJECT_UPDATED, "Project", existingProject.getId(), existingProject.getName());
        return convertToDTO(updated);
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
        projectDTO.setWorkspaceId(project.getWorkspace().getId());
        return projectDTO;
    }
}
