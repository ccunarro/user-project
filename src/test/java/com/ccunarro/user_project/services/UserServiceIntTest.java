package com.ccunarro.user_project.services;

import com.ccunarro.user_project.repositories.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntTest {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserServiceIntTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    void createUserOk() {
        var user = userService.createUser("johnny@gmail.com", "Johnny Jones", "askdljasdfaad");

        var found = userService.findUser(user.id());

        assertNotNull(found);
        assertNotNull(found.id());
        assertEquals("johnny@gmail.com", found.email());
        assertEquals("Johnny Jones", found.name());
        assertTrue(userService.passwordMatches("askdljasdfaad", user.id()));
    }

    @Test
    void addExternalProjectsToUserOk() {
        var user = userService.createUser("johnnyWithProjects@gmail.com", "Johnny Project", "theMostSecurePassword");
        var externalIdProjectOne = "external-id-" + UUID.randomUUID();
        var externalIdProjectTwo = "external-id-" + UUID.randomUUID();
        userService.addExternalProjectToUser(user.id(), externalIdProjectOne);
        userService.addExternalProjectToUser(user.id(), externalIdProjectTwo);

        var userWithProjects = userService.findUserWithExternalProjects(user.id());

        assertEquals(2, userWithProjects.externalProjectIds().size());
        assertTrue(userWithProjects.externalProjectIds().contains(externalIdProjectOne));
        assertTrue(userWithProjects.externalProjectIds().contains(externalIdProjectTwo));
    }

    @Test
    void deleteUserOk() {
        var user = userService.createUser("userToDelete@gmail.com", "Johnny Deleted", "eliminated");
        var externalIdProjectOne = "external-id-" + UUID.randomUUID();
        var externalIdProjectTwo = "external-id-" + UUID.randomUUID();
        userService.addExternalProjectToUser(user.id(), externalIdProjectOne);
        userService.addExternalProjectToUser(user.id(), externalIdProjectTwo);

        userService.deleteUser(user.id());

        var exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.findUser(user.id()));
        assertEquals("User not found for id " + user.id(), exception.getMessage());

        assertEquals(0, userRepository.countExternalProjects(user.id()));
    }

    @Test
    void addRepeatedExternalProjectsToUserShouldFail() {
        var user = userService.createUser("repeatedProjectAdd@gmail.com", "Repeated Project", "theMostSecurePassword");
        var externalIdProject = "external-id-" + UUID.randomUUID();
        userService.addExternalProjectToUser(user.id(), externalIdProject);

        var exception = Assertions.assertThrows(InvalidExternalProjectException.class, () -> userService.addExternalProjectToUser(user.id(), externalIdProject));
        assertEquals("The external project id provided already exists", exception.getMessage());
    }



}