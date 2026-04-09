package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.ProjectRepository;
import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.model.Project;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> dtos = new ArrayList<>();
        for (Project project : projects) {
            dtos.add(convertToDTO(project));
        }
        return dtos;
    }

    public ProjectDTO createProject(Project project) {
        Project saved = projectRepository.save(project);
        return convertToDTO(saved);
    }

    public Project updateProject(Long id, Project project) {
        Project existingProject = getProjectById(id);
        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());
        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long id) {
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
