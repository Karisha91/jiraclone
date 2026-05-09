package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.CommentRepository;
import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.User;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public List<CommentDTO> getCommentsByIssueId(Long issueId){
        List<Comment> comments = commentRepository.findByIssueId(issueId);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment : comments){
            commentDTOS.add(convertCommentToDTO(comment));
        }
        return commentDTOS;
    }

    public List<CommentDTO> getAllComments(){
        List<Comment> comments = commentRepository.findAll();
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for(Comment comment : comments){
            commentDTOs.add(convertCommentToDTO(comment));
        }
       return commentDTOs;
    }

    public CommentDTO convertCommentToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setContent(comment.getContent());
        commentDTO.setAuthor(comment.getAuthor() != null ? comment.getAuthor().getUsername() : null);
        return commentDTO;

    }

    public void deleteCommentById(Long id){
        commentRepository.deleteById(id);
    }


    public Comment addComment(Comment comment ,Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        comment.setAuthor(user);
        comment.setCreatedAt(java.time.LocalDateTime.now());
        return commentRepository.save(comment);

    }


}
