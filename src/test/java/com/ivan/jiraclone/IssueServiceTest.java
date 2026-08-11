package com.ivan.jiraclone;

import com.ivan.jiraclone.Repository.IssueRepository;
import com.ivan.jiraclone.dto.CreateIssueRequest;
import com.ivan.jiraclone.dto.IssueDTO;
import com.ivan.jiraclone.dto.UpdateIssueRequest;
import com.ivan.jiraclone.enums.Priority;
import com.ivan.jiraclone.enums.Status;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.model.Workspace;
import com.ivan.jiraclone.service.AuditLogService;
import com.ivan.jiraclone.service.IssueService;
import com.ivan.jiraclone.service.NotificationService;
import com.ivan.jiraclone.service.ProjectService;
import com.ivan.jiraclone.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueServiceTest {

    private IssueService issueService;
    private IssueRepository issueRepository;
    private UserService userService;
    private AuditLogService auditLogService;
    private ProjectService projectService;
    private JavaMailSender javaMailSender;
    private Principal principal;

    private Project projectWithWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        Project project = new Project();
        project.setId(1L);
        project.setWorkspace(workspace);
        return project;
    }

    @BeforeEach
    public void setup() {
        issueRepository = Mockito.mock(IssueRepository.class);
        userService = Mockito.mock(UserService.class);
        auditLogService = Mockito.mock(AuditLogService.class);
        projectService = Mockito.mock(ProjectService.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);
        NotificationService notificationService = Mockito.mock(NotificationService.class);
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");
        issueService = new IssueService(issueRepository, notificationService, userService, auditLogService, projectService, javaMailSender);
    }

    @Test
    void addIssue() {
        User reporter = new User();
        reporter.setUsername("admin");

        Project project = projectWithWorkspace();

        CreateIssueRequest request = new CreateIssueRequest();
        request.setTitle("Test issue");
        request.setDescription("Test description");
        request.setStatus(Status.TO_DO);
        request.setPriority(Priority.LOW);
        request.setProjectId(1L);
        request.setReporterId(1L);

        Issue issue = new Issue();
        issue.setReporter(reporter);
        issue.setProject(project);
        issue.setTitle("Test issue");

        Mockito.when(userService.getUserById(1L)).thenReturn(reporter);
        Mockito.when(projectService.getProjectById(1L)).thenReturn(project);
        Mockito.when(issueRepository.save(Mockito.any(Issue.class))).thenReturn(issue);

        IssueDTO issueSaved = issueService.addIssue(request, principal);
        assertEquals(issue.getTitle(), issueSaved.getTitle());
    }

    @Test
    void getAllIssues() {
        Project project = projectWithWorkspace();

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);
        Issue issue2 = new Issue();
        issue2.setId(2L);
        issue2.setDescription("Test issue2");
        issue2.setProject(project);

        List<Issue> issues = Arrays.asList(issue, issue2);

        Mockito.when(issueRepository.findAll()).thenReturn(issues);
        List<IssueDTO> issuesDTO = issueService.getAllIssues();

        assertEquals(2, issuesDTO.size());
    }

    @Test
    void getIssueById() {
        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);
        Mockito.when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));

        Issue saved = issueService.getIssueById(issue.getId());
        assertEquals(issue, saved);
    }

    @Test
    void deleteIssueById() {
        User reporter = new User();
        reporter.setUsername("admin");

        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);
        issue.setReporter(reporter);

        Mockito.when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));
        issueService.deleteIssueById(issue.getId(), principal);

        Mockito.verify(issueRepository, Mockito.times(1)).delete(issue);
    }

    @Test
    void updateIssue() {
        User reporter = new User();
        reporter.setUsername("admin");

        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setTitle("Test issue");
        issue.setDescription("Test issue");
        issue.setProject(project);
        issue.setReporter(reporter);

        UpdateIssueRequest request = new UpdateIssueRequest();
        request.setTitle("Updated Test issue");
        request.setDescription("Updated Test issue");
        request.setStatus(Status.TO_DO);
        request.setPriority(Priority.LOW);

        Mockito.when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));
        Mockito.when(issueRepository.save(issue)).thenReturn(issue);

        IssueDTO result = issueService.updateIssue(issue.getId(), request, principal);
        assertEquals("Updated Test issue", result.getTitle());
        Mockito.verify(issueRepository, Mockito.times(1)).save(issue);
    }

    @Test
    void convertIssueToDTO() {
        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);

        IssueDTO issueDTO = issueService.convertToDTO(issue);
        assertEquals(issue.getDescription(), issueDTO.getDescription());
    }

    @Test
    void getIssueByProjectId() {
        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);
        Mockito.when(issueRepository.findByProjectId(issue.getProject().getId())).thenReturn(List.of(issue));

        List<IssueDTO> result = issueService.getIssuesByProjectId(issue.getProject().getId());
        assertEquals(1, result.size());
        assertEquals(issue.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getIssuesByStatus() {
        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);
        Mockito.when(issueRepository.findByStatus(issue.getStatus())).thenReturn(List.of(issue));
        List<IssueDTO> result = issueService.getIssuesByStatus(issue.getStatus());
        assertEquals(1, result.size());
        assertEquals(issue.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getIssuesByProjectIdAndStatus() {
        Project project = projectWithWorkspace();
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setDescription("Test issue");
        issue.setProject(project);

        Mockito.when(issueRepository.findByProjectIdAndStatus(issue.getProject().getId(), issue.getStatus())).thenReturn(List.of(issue));

        List<IssueDTO> result = issueService.getIssuesByProjectIdAndStatus(issue.getProject().getId(), issue.getStatus());
        assertEquals(1, result.size());
        assertEquals(issue.getDescription(), result.getFirst().getDescription());
    }
}