package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.CommentRepository;
import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<CommentDTO> getCommentsByIssueId(Long issueId, Pageable pageable){
        Page<Comment> comments = commentRepository.findByIssueId(issueId, pageable);
        return comments.map(this::convertCommentToDTO);

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
        commentDTO.setAuthorAvatarUrl(comment.getAuthor() != null ? comment.getAuthor().getAvatarUrl() : null);
        return commentDTO;

    }

    public void deleteCommentById(Long id , Principal principal){
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        if(user.getRole().equals("ADMIN") || comment.getAuthor().getId().equals(user.getId())){
            commentRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("You are not authorized to delete this comment");
        }

    }


    public Comment addComment(Comment comment ,Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        comment.setAuthor(user);
        comment.setCreatedAt(java.time.LocalDateTime.now());
        return commentRepository.save(comment);

    }


}
