package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
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
    public Comment addComment(@RequestBody Comment comment) {
       return commentService.addComment(comment);
    }


}
