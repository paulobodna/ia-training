package com.bodna.logic_hub.integration.controllers;


import com.bodna.logic_hub.integration.IntegrationTest;
import com.bodna.logic_hub.repositories.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class JWTIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldAccessWithValidToken() throws Exception {
        String email = "nedstark@got.com";
        String password = "Password123!";

        registerTestUser(email, password);

        String jwt = loginTestUser(email, password);

        assertNotNull(jwt);
        assertFalse(jwt.isBlank());

        mockMvc.perform(get("/history")
            .header("Authorization", "Bearer " + jwt)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldProhibitAccessWithInvalidToken() throws Exception {
        String email = "nedstark@got.com";
        String password = "Password123!";

        registerTestUser(email, password);

        String jwt = loginTestUser(email, password);

        assertNotNull(jwt);
        assertFalse(jwt.isBlank());

        mockMvc.perform(get("/history")
            .header("Authorization", "Bearer " + jwt + "a")
        ).andExpect(status().isUnauthorized());
    }

    //Token ausente
    @Test
    void shouldRedirectToGoogleOAuth2WhenAbsentToken() throws Exception {
        String email = "nedstark@got.com";
        String password = "Password123!";

        registerTestUser(email, password);

        String jwt = loginTestUser(email, password);

        assertNotNull(jwt);
        assertFalse(jwt.isBlank());

        mockMvc.perform(get("/history"))
            .andExpect(status().isFound());
    }

    private void registerTestUser(String email, String password) throws Exception {
        String request = """
            {
                "username": "testUser",
                "email": "%s",
                "password": "%s"
            }
            """.formatted(email, password);

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        );
    }

    private String loginTestUser(String email, String password) throws Exception {
        String loginRequest = """
            {
                "login": "%s",
                "password": "%s"
            }
            """.formatted(email, password);

        String token = mockMvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequest)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return JsonPath.read(token, "$.token");
    }
}