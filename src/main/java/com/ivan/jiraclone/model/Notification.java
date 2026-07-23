package com.ivan.jiraclone.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 2000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = true)
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = true)
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private boolean isRead;

    private LocalDateTime createdAt;

}
