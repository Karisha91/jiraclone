package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.CommentRepository;
import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.model.Project;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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


    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);

    }


}
