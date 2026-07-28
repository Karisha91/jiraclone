package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.UserRepository;
import com.ivan.jiraclone.dto.UserDTO;
import com.ivan.jiraclone.exception.DuplicateResourceException;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;



import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, CloudinaryService cloudinaryService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.jwtUtil = jwtUtil;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public void deleteUserById(Long id) {
         userRepository.deleteById(id);
    }

    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("User with username " + user.getUsername() + " already exists");
        }
        return userRepository.save(user);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->  new ResourceNotFoundException("User not found: " + username));
    }

    public List<UserDTO> getDevelopers() {
        List<User> developers = userRepository.findByRole("DEVELOPER");
        return convertToDTO(developers);
    }
    public List<UserDTO> convertToDTO(List<User> users) {
        List<UserDTO> dtos =  new ArrayList<>();
        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dtos.add(dto);
            dto.setAvatarUrl(user.getAvatarUrl());
        }
        return dtos;
    }

    public ResponseEntity<?> uploadAvatar(Long id, @RequestPart MultipartFile avatar) {
        User user = userRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("User not found with id: " + id));
        String url = cloudinaryService.upload(avatar);
        user.setAvatarUrl(url);
        userRepository.save(user);
        return ResponseEntity.ok(url);
    }

    public String getAvatar(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("User not found with id: " + id));
        return user.getAvatarUrl();

    }

    public User findByEmail(String email) {
       return userRepository.findByEmail(email).orElseThrow(() ->  new ResourceNotFoundException("User not found with email: " + email));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public ResponseEntity<?> updateUsername(Long id, String username) {
        User user =  userRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("User not found: " + id));
        user.setUsername(username);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());
        return ResponseEntity.ok(token);

    }
}
