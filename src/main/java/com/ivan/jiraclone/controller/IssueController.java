package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.IssueDTO;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.service.IssueService;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public void deleteIssueById(@PathVariable Long id) {
        issueService.deleteIssueById(id);
    }


    @PostMapping
    public Issue createIssue(@RequestBody Issue issue) {

        return issueService.addIssue(issue);
    }
    @PutMapping("/{id}")
    public Issue updateIssue(@PathVariable Long id,@RequestBody Issue issue) {
       return issueService.updateIssue(id, issue);
    }
}
