package com.ivan.jiraclone;

import com.ivan.jiraclone.Repository.CommentRepository;
import com.ivan.jiraclone.dto.CommentDTO;
import com.ivan.jiraclone.model.Comment;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.service.CommentService;
import com.ivan.jiraclone.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {



    private CommentService commentService;
    private UserService userService;
    private CommentRepository commentRepository;


    @BeforeEach
    public void setUp(){

        commentRepository = Mockito.mock(CommentRepository.class);
        userService = Mockito.mock(UserService.class);
        commentService = new CommentService(commentRepository,userService);

    }

    @Test
    void getCommentsByIssueId(){
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setIssue(new Issue());

        Mockito.when(commentRepository.findByIssueId(1L)).thenReturn(java.util.List.of(comment));
        List<CommentDTO> commentsDto = commentService.getCommentsByIssueId(1L);

        assertEquals(1, commentsDto.size());
        assertEquals("Test comment", commentsDto.get(0).getContent());

    }

    @Test
    void getAllComments(){
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setIssue(new Issue());
        Mockito.when(commentRepository.findAll()).thenReturn(java.util.List.of(comment));
        List<CommentDTO> commentsDto = commentService.getAllComments();
        assertEquals(1, commentsDto.size());
        assertEquals("Test comment", commentsDto.get(0).getContent());
    }

    @Test
    void deleteCommentById(){
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setIssue(new Issue());
        Mockito.when(commentRepository.findByIssueId(1L)).thenReturn(java.util.List.of(comment));
        commentService.deleteCommentById(1L);
        Mockito.verify(commentRepository,Mockito.times(1)).deleteById(1L);
    }

    @Test
    void addComment(){


        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("ivan");

        User user = new User();
        user.setId(1L);
        user.setUsername("ivan");

        Mockito.when(userService.findByUsername("ivan")).thenReturn(user);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setIssue(new Issue());
        comment.setAuthor(user);
        comment.setCreatedAt(java.time.LocalDateTime.now());

        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        commentService.addComment(comment, principal);


        Mockito.verify(commentRepository,Mockito.times(1)).save(Mockito.any(Comment.class));





    }
}
