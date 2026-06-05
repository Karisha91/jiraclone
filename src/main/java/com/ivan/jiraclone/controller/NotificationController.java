package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.NotificationDTO;
import com.ivan.jiraclone.model.Notification;
import com.ivan.jiraclone.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    private final NotificationService notificationService;





    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;

    }
    @GetMapping("/{userId}")
    public List<NotificationDTO> getNotifications(@PathVariable Long userId) {
       return notificationService.getNotificationsForCurrentUser(userId);

    }
    @PutMapping("/{id}/read")
    public NotificationDTO markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }


}
