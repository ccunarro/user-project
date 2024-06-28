package com.ccunarro.user_project.controllers;

import com.ccunarro.user_project.security.SimpleUserDetails;
import com.ccunarro.user_project.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import jakarta.validation.Valid;

@RestController
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @Autowired
    public UserController (UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/users/")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        var userRecord = userService.createUser(request.getEmail(), request.getName(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(userRecord));
    }

    @PreAuthorize("isAuthenticated() and (@userSecurity.isSuperAdmin(#userDetails) or @userSecurity.isSameUser(#userDetails, #userId))")
    @GetMapping("/users/{userId}/")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId, @AuthenticationPrincipal SimpleUserDetails userDetails) {
        var userRecord = userService.findUser(userId);
        return ResponseEntity.ok(mapper.map(userRecord));
    }

    @PreAuthorize("isAuthenticated() and (@userSecurity.isSuperAdmin(#userDetails) or @userSecurity.isSameUser(#userDetails, #userId))")
    @DeleteMapping("/users/{userId}/")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID userId, @AuthenticationPrincipal SimpleUserDetails userDetails) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.apiResponseOk("Successfully deleted user with id: " + userId));
    }

    @PreAuthorize("isAuthenticated() and (@userSecurity.isSuperAdmin(#userDetails) or @userSecurity.isSameUser(#userDetails, #userId))")
    @PatchMapping("/users/{userId}/external-projects/")
    public ResponseEntity<ApiResponse> addExternalProjectToUser(@PathVariable UUID userId,
                                                                @Valid @RequestBody UserExternalProjectRequest request,
                                                                @AuthenticationPrincipal SimpleUserDetails userDetails) {
        userService.addExternalProjectToUser(userId, request.getExternalProjectId());
        return ResponseEntity.ok(ApiResponse.apiResponseOk("Successfully added external project with id: " + request.getExternalProjectId()));
    }

    @PreAuthorize("isAuthenticated() and (@userSecurity.isSuperAdmin(#userDetails) or @userSecurity.isSameUser(#userDetails, #userId))")
    @GetMapping("/users/{userId}/external-projects/")
    public ResponseEntity<UserExternalProjectsResponse> getUserWithExternalProjects(@PathVariable UUID userId, @AuthenticationPrincipal SimpleUserDetails userDetails) {
        var userWithProjects = userService.findUserWithExternalProjects(userId);
        return ResponseEntity.ok(mapper.map(userWithProjects));
    }
}
