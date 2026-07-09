package com.ivan.jiraclone;

import com.ivan.jiraclone.Repository.IssueRepository;
import com.ivan.jiraclone.dto.IssueDTO;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.service.AuditLogService;
import com.ivan.jiraclone.service.IssueService;
import com.ivan.jiraclone.service.NotificationService;
import com.ivan.jiraclone.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    private Principal principal;

    @BeforeEach
    public void setup() {
        issueRepository = Mockito.mock(IssueRepository.class);
        userService = Mockito.mock(UserService.class);
        auditLogService = Mockito.mock(AuditLogService.class);
        NotificationService notificationService = Mockito.mock(NotificationService.class);
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("admin");
        issueService = new IssueService(issueRepository, notificationService, userService, auditLogService);
    }

    @Test
    void AddIssue() {
        User reporter = new User();
        reporter.setUsername("admin");

        Issue issue = new Issue();
        issue.setReporter(reporter);

        Mockito.when(issueRepository.save(issue)).thenReturn(issue);
        Issue issueSaved = issueService.addIssue(issue, principal);

        assertEquals(issue, issueSaved);
    }

    @Test
    void getAllIssues() {
        Project project = new Project();

        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(project);
        Issue issue2 = new Issue();
        issue2.setId(2);
        issue2.setDescription("Test issue2");
        issue2.setProject(project);

        List<Issue> issues = Arrays.asList(issue, issue2);

        Mockito.when(issueRepository.findAll()).thenReturn(issues);
        List<IssueDTO> issuesDTO = issueService.getAllIssues();

        assertEquals(2, issuesDTO.size());
    }

    @Test
    void getIssueById() {
        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(new Project());
        Mockito.when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));

        Issue saved = issueService.getIssueById(issue.getId());
        assertEquals(issue, saved);
    }

    @Test
    void deleteIssueById() {
        User reporter = new User();
        reporter.setUsername("admin");

        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(new Project());
        issue.setReporter(reporter);

        Mockito.when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));
        issueService.deleteIssueById(issue.getId(), principal);

        Mockito.verify(issueRepository, Mockito.times(1)).delete(issue);
    }

    @Test
    void updateIssue() {
        User reporter = new User();
        reporter.setUsername("admin");

        Issue issue = new Issue();
        issue.setId(1);
        issue.setTitle("Test issue");
        issue.setDescription("Test issue");
        issue.setProject(new Project());
        issue.setReporter(reporter);

        Issue updatedIssue = new Issue();
        updatedIssue.setTitle("Updated Test issue");
        updatedIssue.setDescription("updated Test issue");

        Mockito.when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));
        Mockito.when(issueRepository.save(issue)).thenReturn(issue);

        Issue result = issueService.updateIssue(issue.getId(), updatedIssue);

        assertEquals(issue, result);
        Mockito.verify(issueRepository, Mockito.times(1)).save(issue);
    }

    @Test
    void convertIssueToDTO() {
        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(new Project());

        IssueDTO issueDTO = issueService.convertToDTO(issue);
        assertEquals(issue.getDescription(), issueDTO.getDescription());
    }

    @Test
    void getIssueByProjectId() {
        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(new Project());
        Mockito.when(issueRepository.findByProjectId(issue.getProject().getId())).thenReturn(List.of(issue));

        List<IssueDTO> result = issueService.getIssuesByProjectId(issue.getProject().getId());
        assertEquals(1, result.size());
        assertEquals(issue.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getIssuesByStatus() {
        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(new Project());
        Mockito.when(issueRepository.findByStatus(issue.getStatus())).thenReturn(List.of(issue));
        List<IssueDTO> result = issueService.getIssuesByStatus(issue.getStatus());
        assertEquals(1, result.size());
        assertEquals(issue.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getIssuesByProjectIdAndStatus() {
        Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription("Test issue");
        issue.setProject(new Project());

        Mockito.when(issueRepository.findByProjectIdAndStatus(issue.getProject().getId(), issue.getStatus())).thenReturn(List.of(issue));

        List<IssueDTO> result = issueService.getIssuesByProjectIdAndStatus(issue.getProject().getId(), issue.getStatus());
        assertEquals(1, result.size());
        assertEquals(issue.getDescription(), result.get(0).getDescription());
    }
}