package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.IssueRepository;
import com.ivan.jiraclone.Repository.NotificationRepository;
import com.ivan.jiraclone.Repository.UserRepository;
import com.ivan.jiraclone.dto.NotificationDTO;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.model.Notification;
import com.ivan.jiraclone.model.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;



    public NotificationService(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository, UserRepository userRepository, IssueRepository issueRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
    }


    public void sendNotification(Long userId, String message, Long issueId) {
        Notification notification  = saveNotification(userId, message, issueId);
        NotificationDTO notificationDTO = new NotificationDTO(message, issueId, notification.getId(), notification.isRead());

        messagingTemplate.convertAndSend(
                "/queue/notifications/" + userId,
                notificationDTO
        );
    }

    public Notification saveNotification(Long userId, String message, Long issueId) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setIssue(issueRepository.findById(issueId).orElseThrow(() ->  new ResourceNotFoundException("Issue not found")));
        notification.setUser(userRepository.findById(userId).orElseThrow(() ->  new ResourceNotFoundException("User not found")));
        notification.setRead(false);
        notification.setCreatedAt(java.time.LocalDateTime.now());
        return notificationRepository.save(notification);

    }

    public List<NotificationDTO> getNotificationsForCurrentUser(Long userId) {
        return convertToDto(notificationRepository.findByUserId(userId));

    }

    public List<NotificationDTO> convertToDto(List<Notification> notifications) {
        List<NotificationDTO> dtos = new ArrayList<>();
        for (Notification notification : notifications) {
            dtos.add(new NotificationDTO(notification.getMessage(), notification.getIssue().getId(), notification.getId(), notification.isRead()));
        }
        return dtos;
    }

    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return new NotificationDTO(notification.getMessage(), notification.getIssue().getId(), notification.getId(), notification.isRead());
    }

    public void deleteNotificationsByIssueId(Long id) {
        List<Notification> notifications = notificationRepository.findByIssueId(id);
        notificationRepository.deleteAll(notifications);
    }


}

