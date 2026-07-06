package com.ivan.jiraclone.controller;


import com.ivan.jiraclone.dto.UserDTO;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/developers")
    public List<UserDTO> getDevelopers() {
        return userService.getDevelopers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

    }
    @PutMapping("/{id}/upload")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id, @RequestPart MultipartFile avatar ) {

        return  userService.uploadAvatar(id, avatar);

    }

    @GetMapping("/{id}/avatar")
    public String getAvatar(@PathVariable Long id) {
        return userService.getAvatar(id);
    }
}
