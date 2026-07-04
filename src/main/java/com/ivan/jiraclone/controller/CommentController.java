package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.service.CommentService;
import com.ivan.jiraclone.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;



    public CommentController(CommentService commentService) {
        this.commentService = commentService;

    }

    @GetMapping("/{id}")
    public Page<CommentDTO> getCommentsByIssueId(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getCommentsByIssueId(id, pageable);
    }

    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCommentById(@PathVariable Long id){
        commentService.deleteCommentById(id);
    }

    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    @PostMapping
    public CommentDTO addComment(@RequestBody Comment comment, Principal principal) {
        return commentService.convertCommentToDTO(commentService.addComment(comment, principal));
    }


}
