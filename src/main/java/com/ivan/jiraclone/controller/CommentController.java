package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.service.CommentService;
import com.ivan.jiraclone.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public List<CommentDTO> getCommentsByIssueId(@PathVariable Long id) {
        return commentService.getCommentsByIssueId(id);
    }
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    @DeleteMapping("/{id}")
    public void deleteCommentById(@PathVariable Long id){
        commentService.deleteCommentById(id);
    }


    @PostMapping
    public CommentDTO addComment(@RequestBody Comment comment, Principal principal) {
        return commentService.convertCommentToDTO(commentService.addComment(comment, principal));
    }


}
