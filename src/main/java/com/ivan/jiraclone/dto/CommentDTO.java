package com.ivan.jiraclone.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String author;

}
