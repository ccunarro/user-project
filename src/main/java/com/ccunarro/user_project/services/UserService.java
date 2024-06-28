package com.ccunarro.user_project.services;

import com.ccunarro.user_project.repositories.User;
import com.ccunarro.user_project.repositories.UserExternalProject;
import com.ccunarro.user_project.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRecord findUser(UUID userId) {
        return userRepository.findById(userId)
                .map(u -> new UserRecord(u.getId(), u.getEmail(), u.getName()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id " + userId));
    }

    public UserExternalProjectsRecord findUserWithExternalProjects(UUID userId) {
        User user = userRepository.findUserWithExternalProjects(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found for id " + userId);
        }
        var externalProjectIds = user.getExternalProjects().stream().map(UserExternalProject::getExternalProjectId).toList();

        return new UserExternalProjectsRecord(user.getId(), user.getEmail(), user.getName(), externalProjectIds);
    }

    public void addExternalProjectToUser(UUID userId, String externalProjectId) {
        if (userRepository.existsExternalProject(externalProjectId)) {
            throw new InvalidExternalProjectException("The external project id provided already exists");
        }

        User user = userRepository.findUserWithExternalProjects(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found for id " + userId);
        }

        var externalProject = new UserExternalProject(user, externalProjectId);
        user.getExternalProjects().add(externalProject);
        userRepository.save(user);
    }

    public UserRecord createUser(String email, String name, String password) {
        if (userRepository.existsUserWithEmail(email)) {
            throw new BadInputException("Email is already in use");
        }

        User u = new User();
        u.setEmail(email);
        u.setName(name);
        u.setPassword(passwordEncoder.encode(password));
        u = userRepository.save(u);
        return new UserRecord(u.getId(), u.getEmail(), u.getName());
    }

    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found for id " + userId);
        }
        userRepository.deleteById(userId);
    }

    public boolean passwordMatches(String rawPassword, UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for id " + userId));
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
