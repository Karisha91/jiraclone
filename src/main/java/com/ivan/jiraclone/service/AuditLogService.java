package com.ivan.jiraclone.service;

import com.ivan.jiraclone.Repository.AuditLogRepository;
import com.ivan.jiraclone.enums.AuditAction;
import com.ivan.jiraclone.model.AuditLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private AuditLogRepository auditLogRepository;


    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }



    public AuditLog logAction(String username, AuditAction action, String entityType, Long entityId, String entityName) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setEntityName(entityName);
        auditLog.setOccurredAt(LocalDateTime.now());
        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}

