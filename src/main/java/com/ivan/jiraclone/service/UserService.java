package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.UserRepository;
import com.ivan.jiraclone.dto.UserDTO;
import com.ivan.jiraclone.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUserById(Long id) {
         userRepository.deleteById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
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
        }
        return dtos;
    }

}
