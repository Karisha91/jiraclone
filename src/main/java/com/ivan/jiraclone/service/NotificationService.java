package com.ivan.jiraclone.service;


import com.ivan.jiraclone.dto.NotificationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;


    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public void sendNotification(Long userId, String message, Long issueId) {
        NotificationDTO notification = new NotificationDTO(message, issueId);
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + userId,
                notification
        );
    }

}
