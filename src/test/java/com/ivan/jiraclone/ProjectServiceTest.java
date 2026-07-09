package com.ivan.jiraclone;

import com.ivan.jiraclone.Repository.ProjectRepository;
import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.service.AuditLogService;
import com.ivan.jiraclone.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;
    private AuditLogService auditLogService;
    private Principal principal;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        auditLogService = Mockito.mock(AuditLogService.class);
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");
        projectService = new ProjectService(projectRepository, auditLogService);
    }

    @Test
    void getAllProjects() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("E-commerce App");
        project1.setDescription("Online store");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Mobile Game");
        project2.setDescription("Android/iOS game");

        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);

        Mockito.when(projectRepository.findAll()).thenReturn(projects);
        List<ProjectDTO> dtos = projectService.getAllProjects();

        assertEquals(2, dtos.size());
        assertEquals("E-commerce App", dtos.get(0).getProjectName());
        assertEquals("Mobile Game", dtos.get(1).getProjectName());
    }

    @Test
    void getProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("E-commerce App");
        project.setDescription("Online store");
        Mockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertEquals(project, result);
    }

    @Test
    void createProject() {
        Project project = new Project();
        project.setName("E-commerce App");
        project.setDescription("Online store");
        Mockito.when(projectRepository.save(project)).thenReturn(project);
        ProjectDTO projectDTO = projectService.createProject(project, principal);
        assertEquals(project.getName(), projectDTO.getProjectName());
        Mockito.verify(projectRepository, Mockito.times(1)).save(project);
    }

    @Test
    void updateProject() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("E-commerce App");
        existingProject.setDescription("Online store");

        Project updatedProject = new Project();
        updatedProject.setName("Updated name");
        updatedProject.setDescription("Updated description");

        Mockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Project result = projectService.updateProject(1L, updatedProject, principal);

        assertEquals("Updated description", existingProject.getDescription());
        assertEquals("Updated name", existingProject.getName());
        assertEquals(updatedProject.getDescription(), result.getDescription());
        assertEquals(existingProject.getId(), result.getId());

        Mockito.verify(projectRepository, Mockito.times(1)).save(existingProject);
    }

    @Test
    void deleteProject() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(existingProject));

        projectService.deleteProject(projectId, principal);
        Mockito.verify(projectRepository, Mockito.times(1)).deleteById(projectId);
    }

    @Test
    void convertProjectToDTO() {
        Project project = new Project();
        project.setId(1L);
        project.setName("E-commerce App");
        project.setDescription("Online store");

        ProjectDTO projectDTO = projectService.convertToDTO(project);
        assertEquals(project.getName(), projectDTO.getProjectName());
        assertEquals(project.getDescription(), projectDTO.getDescription());
    }
}