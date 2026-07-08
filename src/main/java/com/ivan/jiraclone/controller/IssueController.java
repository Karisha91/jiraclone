package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.AssignRequest;
import com.ivan.jiraclone.dto.IssueDTO;
import com.ivan.jiraclone.enums.Status;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.service.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;


    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<IssueDTO> getAllIssues() {
        return issueService.getAllIssues();
    }

    @GetMapping("/{id}")
    public IssueDTO getIssueById(@PathVariable Long id) {
        return issueService.getIssueDTOById(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteIssueById(@PathVariable Long id) {
        issueService.deleteIssueById(id);
    }

    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    @PostMapping
    public Issue createIssue(@RequestBody Issue issue) {

        return issueService.addIssue(issue);
    }
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Issue updateIssue(@PathVariable Long id,@RequestBody Issue issue) {
       return issueService.updateIssue(id, issue);
    }


    @GetMapping("/project/{projectId}")
    public Page<IssueDTO> getIssuesByProjectId(@PathVariable Long projectId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return issueService.getIssuesByProjectId(projectId, pageable);
    }

    @GetMapping("/status/{status}")
    public List<IssueDTO> getIssuesByStatus(@PathVariable Status status) {
        return issueService.getIssuesByStatus(status);
    }

    @GetMapping("project/{projectId}/status/{status}")
    public List<IssueDTO> getIssuesByProjectIdAndStatus(@PathVariable Long projectId,@PathVariable Status status) {
        return issueService.getIssuesByProjectIdAndStatus(projectId, status);
    }
    @GetMapping("assignee/{assigneeId}")
    public List<IssueDTO> getIssuesByAssigneeId(@PathVariable Long assigneeId) {
        return issueService.getIssuesByAssigneeId(assigneeId);
    }
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    @PutMapping("/{id}/assign")
    public IssueDTO assignIssue(@PathVariable Long id,@RequestBody AssignRequest assignRequest) {
        return issueService.assignIssue(id, assignRequest.getAssigneeId());


    }
    @GetMapping("/my-assigned")
    public List<IssueDTO> getIssuesAssignedToMe(Principal principal) {
        String username = principal.getName();
        return issueService.getIssuesAssignedToUser(username);
    }
}





