package com.ivan.jiraclone.service;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;


    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public void sendNotification(Long userId, String message) {
        System.out.println("Sending notification to userId: " + userId + " message: " + message);
        messagingTemplate.convertAndSend(
                "/queue/notifications/" + userId,
                message
        );
    }

}
