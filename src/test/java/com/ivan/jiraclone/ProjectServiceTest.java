package com.ivan.jiraclone;

import com.ivan.jiraclone.Repository.ProjectRepository;
import com.ivan.jiraclone.dto.CreateProjectRequest;
import com.ivan.jiraclone.dto.ProjectDTO;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.Workspace;
import com.ivan.jiraclone.service.AuditLogService;
import com.ivan.jiraclone.service.ProjectService;
import com.ivan.jiraclone.service.WorkspaceService;
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
    private WorkspaceService workspaceService;
    private Principal principal;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        auditLogService = Mockito.mock(AuditLogService.class);
        workspaceService = Mockito.mock(WorkspaceService.class);
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");
        projectService = new ProjectService(projectRepository, auditLogService, workspaceService);
    }

    @Test
    void getAllProjectsByWorkspaceId() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);

        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("E-commerce App");
        project1.setDescription("Online store");
        project1.setWorkspace(workspace);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Mobile Game");
        project2.setDescription("Android/iOS game");
        project2.setWorkspace(workspace);

        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);

        Mockito.when(projectRepository.findByWorkspaceId(1L)).thenReturn(projects);
        List<ProjectDTO> dtos = projectService.getAllProjectsByWorkspaceId(1L);

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
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        Project project = new Project();
        project.setName("E-commerce App");
        project.setDescription("Online store");
        project.setWorkspace(workspace);

        CreateProjectRequest request = new CreateProjectRequest();
        request.setProjectName("E-commerce App");
        request.setDescription("Online store");
        request.setWorkspaceId(1L);

        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);
        ProjectDTO projectDTO = projectService.createProject(request, principal);
        assertEquals(project.getName(), projectDTO.getProjectName());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
    }

    @Test
    void updateProject() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("E-commerce App");
        existingProject.setDescription("Online store");
        existingProject.setWorkspace(workspace);

        CreateProjectRequest request = new CreateProjectRequest();
        request.setProjectName("Updated name");
        request.setDescription("Updated description");

        Mockito.when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectRepository.save(existingProject)).thenReturn(existingProject);

        ProjectDTO result = projectService.updateProject(1L, request, principal);

        assertEquals("Updated description", existingProject.getDescription());
        assertEquals("Updated name", existingProject.getName());
        Mockito.verify(projectRepository, Mockito.times(1)).save(existingProject);
    }

    @Test
    void deleteProject() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

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