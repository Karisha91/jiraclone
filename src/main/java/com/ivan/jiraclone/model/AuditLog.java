package com.ivan.jiraclone.model;


import com.ivan.jiraclone.enums.AuditAction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Enumerated(EnumType.STRING)
    private AuditAction action;
    private LocalDateTime occurredAt;
    private String entityType;
    private Long entityId;
    private String entityName;



}
