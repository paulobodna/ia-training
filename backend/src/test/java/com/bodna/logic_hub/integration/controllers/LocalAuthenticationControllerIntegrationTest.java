package com.bodna.logic_hub.integration.controllers;


import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.integration.IntegrationTest;
import com.bodna.logic_hub.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LocalAuthenticationControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        String request = """
            {
                "username": "nedstark",
                "email": "nedstark@got.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());

        assertThat(
            userRepository.findByEmail("nedstark@got.com")
        ).isPresent();
    }

    @Test
    void shouldRejectBlankUsernameSignupRequest() throws Exception {
        String request = """
            {
                "username": "   ",
                "email": "nedstark@got.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNullUsernameSignupRequest() throws Exception {
        String request = """
            {
                "username": "",
                "email": "nedstark@got.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectUsernameLesserThan3CharsSignupRequest() throws Exception {
        String username = "ne";

        String request = """
            {
                "username": "%s",
                "email": "nedstark@got.com",
                "password": "Password123!"
            }
            """.formatted(username);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectUsernameBiggerThan20CharsSignupRequest() throws Exception {
        String username = "a".repeat(21);

        String request = """
            {
                "username": "%s",
                "email": "nedstark@got.com",
                "password": "Password123!"
            }
            """.formatted(username);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectBlankEmailSignupRequest() throws Exception {
        String request = """
            {
                "username": "nedstark",
                "email": "   ",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNullEmailSignupRequest() throws Exception {
        String request = """
            {
                "username": "nedstark",
                "email": "",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectEmailLesserThan6CharsSignupRequest() throws Exception {
        String email = "a@b.c";

        String request = """
            {
                "username": "nedstark",
                "email": "%s",
                "password": "Password123!"
            }
            """.formatted(email);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectEmailBiggerThan254CharsSignupRequest() throws Exception {
        String email = "a".repeat(254) + "@email.com";

        String request = """
            {
                "username": "nedstark",
                "email": "%s",
                "password": "Password123!"
            }
            """.formatted(email);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectBlankPasswordSignupRequest() throws Exception {
        String request = """
            {
                "username": "nedstark",
                "email": "nedstark@got.com",
                "password": "   "
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNullPasswordSignupRequest() throws Exception {
        String request = """
            {
                "username": "nedstark",
                "email": "nedstark@got.com",
                "password": ""
            }
            """;

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectSignupWithExistingEmail() throws Exception {
        String email = "nedstark@got.com";

        userRepository.saveAndFlush(
            User.createLocalUser(email, "nedstark", "password")
        );

        assertTrue(userRepository.existsByEmail(email));

        String request = """
            {
                "username": "another-user",
                "email": "%s",
                "password": "Password123!"
            }
            """.formatted(email);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isConflict());
    }

    @Test
    void shouldRejectSignupWithExistingUsername() throws Exception {
        String username = "nedstark";

        userRepository.saveAndFlush(
            User.createLocalUser("nedstark@got.com", username, "password")
        );

        assertTrue(userRepository.existsByUsername(username));

        String request = """
            {
                "username": "%s",
                "email": "otheruser@email.com",
                "password": "Password123!"
            }
            """.formatted(username);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isConflict());
    }

    @Test
    void shouldLoginUser() throws Exception {
        String email = "nedstark@got.com";
        String password = "Password123!";

        String registerRequest = """
            {
                "username": "nedstark",
                "email": "%s",
                "password": "%s"
            }
            """.formatted(email, password);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest)
        ).andExpect(status().isCreated());

        assertTrue(userRepository.existsByEmail(email));

        String loginRequest = """
            {
                "login": "%s",
                "password": "%s"
            }
            """.formatted(email, password);

        mockMvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequest)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldRejectInvalidPassword() throws Exception {
        String email = "nedstark@got.com";

        String registerRequest = """
            {
                "username": "nedstark",
                "email": "%s",
                "password": "Password123!"
            }
            """.formatted(email);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest)
        ).andExpect(status().isCreated());

        assertTrue(userRepository.existsByEmail(email));

        String loginRequest = """
            {
                "login": "%s",
                "password": "WrongPassword"
            }
            """.formatted(email);

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest)
        ).andExpect(status().isUnauthorized());
    }
}