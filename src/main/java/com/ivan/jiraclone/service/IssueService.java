package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.IssueRepository;
import com.ivan.jiraclone.dto.IssueDTO;
import com.ivan.jiraclone.enums.Status;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ivan.jiraclone.enums.AuditAction;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final AuditLogService auditLogService;


    public IssueService(IssueRepository issueRepository, NotificationService notificationService, UserService userService,AuditLogService auditLogService) {
        this.issueRepository = issueRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.auditLogService = auditLogService;
    }

    public Issue addIssue(Issue issue, Principal principal) {
        Issue saved = issueRepository.save(issue);
        auditLogService.logAction(principal.getName(), AuditAction.ISSUE_CREATED, "Issue", saved.getId(), saved.getTitle());
        return saved;
    }

    public List<IssueDTO> getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        List<IssueDTO> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            dtos.add(convertToDTO(issue));
        }
        return dtos;
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Issue not found with id: " + id));
    }

    public IssueDTO getIssueDTOById(Long id) {
        return convertToDTO(getIssueById(id));
    }

    public void deleteIssueById(Long id, Principal principal) {
        Issue issue = getIssueById(id);
        notificationService.deleteNotificationsByIssueId(id);
        auditLogService.logAction(principal.getName(), AuditAction.ISSUE_DELETED, "Issue", issue.getId(), issue.getTitle());
        issueRepository.delete(issue);
    }

    public Issue updateIssue(Long id, Issue issue) {
        Issue existing = getIssueById(id);
        existing.setTitle(issue.getTitle());
        existing.setDescription(issue.getDescription());
        existing.setPriority(issue.getPriority());
        existing.setStatus(issue.getStatus());


        if (issue.getAssignee() != null) {
            boolean assigneeChanged = existing.getAssignee() == null ||
                    !existing.getAssignee().getId().equals(issue.getAssignee().getId());


            if (assigneeChanged) {
                existing.setAssignee(issue.getAssignee());
                notificationService.sendNotification(
                        issue.getAssignee().getId(),
                        "You have been assigned to: " + issue.getTitle(), id
                );
            }
        }
        auditLogService.logAction(existing.getReporter().getUsername(), AuditAction.ISSUE_UPDATED, "Issue", existing.getId(), existing.getTitle());

        return issueRepository.save(existing);
    }

    public IssueDTO convertToDTO(Issue issue) {
        IssueDTO dto = new IssueDTO();
        dto.setId(issue.getId());
        dto.setProjectName(issue.getProject().getName());
        dto.setReporterUsername(issue.getReporter() != null ? issue.getReporter().getUsername() : null);
        dto.setAssigneeUsername(issue.getAssignee() != null ? issue.getAssignee().getUsername() : null);
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setPriority(issue.getPriority());
        dto.setStatus(issue.getStatus());
        dto.setProjectId(issue.getProject().getId());
        dto.setAssigneeAvatarUrl(issue.getAssignee() != null ? issue.getAssignee().getAvatarUrl() : null);
        dto.setReporterAvatarUrl(issue.getReporter() != null ? issue.getReporter().getAvatarUrl() : null);

        return dto;
    }

    public List<IssueDTO> getIssuesByProjectId(Long projectId) {
        List<Issue> issues = issueRepository.findByProjectId(projectId);
        List<IssueDTO> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            dtos.add(convertToDTO(issue));
        }
        return dtos;
    }

    public Page<IssueDTO> getIssuesByProjectId(Long projectId, Pageable pageable) {
        Page<Issue> issues = issueRepository.findByProjectId(projectId, pageable);
        return issues.map(this::convertToDTO);
    }

    public List<IssueDTO> getIssuesByStatus(Status status) {
        List<Issue> issues = issueRepository.findByStatus(status);
        List<IssueDTO> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            dtos.add(convertToDTO(issue));
        }
        return dtos;
    }

    public List<IssueDTO> getIssuesByProjectIdAndStatus(Long projectId, Status status) {
        List<Issue> issues = issueRepository.findByProjectIdAndStatus(projectId, status);
        List<IssueDTO> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            dtos.add(convertToDTO(issue));
        }
        return dtos;
    }

    public List<IssueDTO> getIssuesByAssigneeId(Long assigneeId) {
        List<Issue> issues = issueRepository.findByAssignee(assigneeId);
        List<IssueDTO> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            dtos.add(convertToDTO(issue));
        }

        return dtos;
    }


    public IssueDTO assignIssue(Long id, Long assigneeId,Principal principal) {
        Issue issue = getIssueById(id);
        User user = userService.getUserById(assigneeId);
        issue.setAssignee(user);
        issueRepository.save(issue);
        notificationService.sendNotification(
                assigneeId,
                "You have been assigned to: " + issue.getTitle(), id
        );
        auditLogService.logAction(principal.getName(), AuditAction.ISSUE_ASSIGNED, "Issue", issue.getId(), issue.getTitle());
        return convertToDTO(issue);

    }

    public List<IssueDTO> getIssuesAssignedToUser(String username) {
        List<Issue> issues = issueRepository.findByAssigneeUsername(username);
        List<IssueDTO> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            dtos.add(convertToDTO(issue));

        }

        return dtos;
    }
}