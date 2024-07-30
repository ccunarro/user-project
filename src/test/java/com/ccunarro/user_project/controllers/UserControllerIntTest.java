package com.ccunarro.user_project.controllers;

import com.ccunarro.user_project.repositories.UserRepository;
import com.ccunarro.user_project.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SUPER_ADMIN_EMAIL = "thesuperadmin@company.com";
    private static final String SUPER_ADMIN_PASSWORD = "theMostSecurePassword";

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUserOk() throws Exception {
        var email = "johncontroller@gmail.com";
        var password = "ThePass1321231322313";
        var user = userService.createUser(email, "Kontroller", password);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/", user.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic(email, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.id().toString()))
                .andExpect(jsonPath("$.name").value(user.name()))
                .andExpect(jsonPath("$.email").value(user.email()));
    }

    @Test
    void testCreateUserOk() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("new@email.com");
        request.setName("My Name");
        request.setPassword("My Passwordddd");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    void testGetUserWithExternalProjectsOk() throws Exception {
        var email = "userWithProjects@gmail.com";
        var password = "ThePass1321231322313";
        var user = userService.createUser(email, "Kontroller", password);
        var externalIdOne = "1-ext-id" + UUID.randomUUID();
        var externalIdTwo = "2-ext-id" + UUID.randomUUID();
        userService.addExternalProjectToUser(user.id(), externalIdOne);
        userService.addExternalProjectToUser(user.id(), externalIdTwo);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/external-projects/", user.id())
                        .with(httpBasic(email, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.id().toString()))
                .andExpect(jsonPath("$.name").value(user.name()))
                .andExpect(jsonPath("$.email").value(user.email()))
                .andExpect(jsonPath("$.externalProjectIds").isArray())
                .andExpect(jsonPath("$.externalProjectIds", hasSize(2)));
    }

    @Test
    void testDeleteUserOk() throws Exception {
        var email = "toBeDeleted@gmail.com";
        var password = "asldkjdsalkdjas";
        var user = userService.createUser(email, "Tobe Deleted", password);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/", user.id())
                        .with(httpBasic(email, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted user with id: " + user.id()));
    }

    @Test
    void testCreateUserInvalidEmailAndPassword() throws Exception {
        UserRequest request = new UserRequest();
        //email has wrong format
        request.setEmail("broken email");
        request.setName("My Name");
        //min length is 8 for the password, so it does not meet minimum requirement
        request.setPassword("111");

        System.out.println("It should properly return a list of errors for the email and password fields that are not correct");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0]").value("Invalid parameter 'email' must be a well-formed email address"))
                .andExpect(jsonPath("$.errors[1]").value("Invalid parameter 'password' size must be between 8 and 50"));
    }

    @Test
    void testCreateUserEmptyPasswordFail() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("myemail@gmail.com");
        request.setName("My Name");

        request.setPassword("");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Invalid parameter 'password' size must be between 8 and 50"));
    }

    @Test
    void testCreateUserDuplicatedEmail() throws Exception {
        var email = "duplikated@gmail.com";
        userService.createUser(email, "Dupli Kated", "0394934sfhajdfhclsahds");

        UserRequest request = new UserRequest();
        request.setEmail(email);
        request.setName("My Name");
        request.setPassword("342344iuj23");

        System.out.println("It should not allow to create a user if the email already exists in the system");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Email is already in use"));
    }

    @Test
    void testGetUserNotFound() throws Exception {
        var inexistentUserId = UUID.randomUUID();
        userService.createUser(SUPER_ADMIN_EMAIL, "SUPER ADMIN", SUPER_ADMIN_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/", inexistentUserId)
                        .with(httpBasic(SUPER_ADMIN_EMAIL, SUPER_ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("User not found for id " + inexistentUserId));
    }

    @Test
    void testGetUserWithNoAuthentication() throws Exception {
        var email = "unauthenticated@gmail.com";
        var password = "saoaifca32329";
        var user = userService.createUser(email, "No Authentication", password);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/", user.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAddProjectToDifferentUserShouldFail() throws Exception {
        var email1 = "user1@gmail.com";
        var password1 = "12345678";
        userService.createUser(email1, "User One", password1);

        var user2 = userService.createUser("user2@gmail.com", "User Two", "1234567822");

        System.out.println("A user should not be allowed to add an external project to another user");

        UserExternalProjectRequest request = new UserExternalProjectRequest();
        request.setExternalProjectId("external-project-7777");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}/external-projects/", user2.id())
                        .with(httpBasic(email1, password1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Unauthorized to perform this action"));
    }

    @Test
    void testDeleteDifferentUserShouldFail() throws Exception {
        var email1 = "user1@gmail.com";
        var password1 = "12345678";
        userService.createUser(email1, "User One", password1);

        var user2 = userService.createUser("user2@gmail.com", "User Two", "1234567822");

        System.out.println("A user should not be allowed to delete another user that is not him");

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/", user2.id())
                        .with(httpBasic(email1, password1)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Unauthorized to perform this action"));
    }

}