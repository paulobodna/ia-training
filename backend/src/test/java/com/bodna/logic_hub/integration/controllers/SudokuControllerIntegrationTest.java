package com.bodna.logic_hub.integration.controllers;

import com.bodna.logic_hub.fixture.SudokuTestBoards;
import com.bodna.logic_hub.integration.IntegrationTest;
import com.bodna.logic_hub.repositories.SudokuAttemptRepository;
import com.bodna.logic_hub.repositories.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SudokuControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SudokuAttemptRepository attemptRepository;

    @BeforeEach
    void setUp() {
        attemptRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateValidSudokuAttempt() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldCreateMultipleEqualValidSudokuAttempts() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldCreateMultipleDifferentValidSudokuAttempts() throws Exception {
        String jwt = createAndLoginTestUser();

        String request123456 = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        String request0 = """
            {
              "solution": %s,
              "seed": 0,
              "generatorVersion": 1,
              "difficulty": 0,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T11:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed0OnEasy());

        String request123 = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T11:48:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request123456)
        ).andExpect(status().isCreated());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request0)
        ).andExpect(status().isCreated());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request123)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldThrowBadRequestWhenInvalidSudokuSeed() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenEmptySudokuAttempt() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
            }
            """;

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenNoSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": {},
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """;

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenEmptySudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringValidEmptyValuesBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenZeroedSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringValidZeroedValuesBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenHighValuesSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringInvalidHighValuesBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenLowValuesSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringInvalidLowValuesBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenBadFormattedValuesSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringPhraseInvalidBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenLowCharValuesSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringInvalidLowCharBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenHighCharValuesSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        ObjectMapper objectMapper = new ObjectMapper();

        String solution = objectMapper.writeValueAsString(
            SudokuTestBoards.stringInvalidHighCharBoard()
        );

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solution);

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenInvalidHigherColumnsSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(invalidHigherColumnsBoard());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenInvalidLesserColumnsSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(invalidLesserColumnsBoard());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenInvalidHigherRowsSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(invalidHigherRowsBoard());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenInvalidLesserRowsSudokuBoard() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(invalidLesserRowsBoard());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateDifferentDifficultiesValidSudokuAttempts() throws Exception {
        String jwt = createAndLoginTestUser();

        String requestEasy = """
            {
              "solution": %s,
              "seed": 0,
              "generatorVersion": 1,
              "difficulty": 0,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T11:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnEasy());

        String requestMedium = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        String requestHard = """
            {
              "solution": %s,
              "seed": 123,
              "generatorVersion": 1,
              "difficulty": 2,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T11:48:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnHard());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestEasy)
        ).andExpect(status().isCreated());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMedium)
        ).andExpect(status().isCreated());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestHard)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldCreateSudokuAttemptWithTips() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 3,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldThrowBadRequestWhenTipsHigherThan65() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 66,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenNegativeTips() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": -1,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S"
            }
            """.formatted(solutionForSeed123456OnMedium());

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenFutureCompletedAt() throws Exception {
        String jwt = createAndLoginTestUser();

        String request = """
            {
              "solution": %s,
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": %s,
              "timeElapsed": "PT30M5S"
            }
            """.formatted(
            solutionForSeed123456OnMedium(),
            Instant.now().plusSeconds(3600).toString()
        );

        mockMvc.perform(
            post("/sudoku")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
    }

    private String createAndLoginTestUser() throws Exception {
        String email = "nedstark@got.com";
        String password = "Password123!";

        registerTestUser(email, password);

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
            .andReturn()
            .getResponse()
            .getContentAsString();

        return JsonPath.read(token, "$.token");
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

    private String solutionForSeed123456OnEasy() {
        return """
                [
                    ["3", "7", "5", "1", "6", "9", "2", "8", "4"],
                    ["9", "6", "4", "2", "5", "8", "1", "3", "7"],
                    ["2", "8", "1", "4", "7", "3", "6", "5", "9"],
                    ["8", "4", "2", "3", "9", "1", "5", "7", "6"],
                    ["6", "1", "3", "7", "2", "5", "9", "4", "8"],
                    ["7", "5", "9", "6", "8", "4", "3", "1", "2"],
                    ["1", "9", "6", "8", "3", "7", "4", "2", "5"],
                    ["5", "3", "8", "9", "4", "2", "7", "6", "1"],
                    ["4", "2", "7", "5", "1", "6", "8", "9", "3"]
                ]
            """;
    }

    private String solutionForSeed123456OnMedium() {
        return """
                [
                    ["2", "1", "9", "4", "5", "6", "3", "8", "7"],
                    ["8", "5", "6", "3", "7", "9", "2", "4", "1"],
                    ["7", "3", "4", "2", "8", "1", "5", "6", "9"],
                    ["4", "2", "7", "1", "9", "8", "6", "3", "5"],
                    ["1", "6", "3", "7", "2", "5", "8", "9", "4"],
                    ["5", "9", "8", "6", "4", "3", "7", "1", "2"],
                    ["9", "7", "1", "8", "3", "2", "4", "5", "6"],
                    ["6", "8", "2", "5", "1", "4", "9", "7", "3"],
                    ["3", "4", "5", "9", "6", "7", "1", "2", "8"]
                ]
            """;
    }

    private String solutionForSeed123456OnHard() {
        return """
                [
                    ["5", "9", "3", "7", "6", "1", "2", "4", "8"],
                    ["8", "1", "6", "4", "9", "2", "3", "5", "7"],
                    ["7", "4", "2", "5", "3", "8", "1", "6", "9"],
                    ["4", "6", "8", "2", "5", "9", "7", "1", "3"],
                    ["3", "2", "1", "8", "7", "6", "4", "9", "5"],
                    ["9", "7", "5", "3", "1", "4", "6", "8", "2"],
                    ["2", "5", "9", "6", "4", "3", "8", "7", "1"],
                    ["6", "8", "7", "1", "2", "5", "9", "3", "4"],
                    ["1", "3", "4", "9", "8", "7", "5", "2", "6"]
                ]
            """;
    }

    private String solutionForSeed0OnEasy() {
        return """
                [
                    ["3", "7", "5", "1", "6", "9", "2", "8", "4"],
                    ["9", "6", "4", "2", "5", "8", "1", "3", "7"],
                    ["2", "8", "1", "4", "7", "3", "6", "5", "9"],
                    ["8", "4", "2", "3", "9", "1", "5", "7", "6"],
                    ["6", "1", "3", "7", "2", "5", "9", "4", "8"],
                    ["7", "5", "9", "6", "8", "4", "3", "1", "2"],
                    ["1", "9", "6", "8", "3", "7", "4", "2", "5"],
                    ["5", "3", "8", "9", "4", "2", "7", "6", "1"],
                    ["4", "2", "7", "5", "1", "6", "8", "9", "3"]
                ]
            """;
    }

    private String solutionForSeed123() {
        return """
                [
                    ["7", "6", "1", "3", "2", "9", "8", "4", "5"],
                    ["4", "3", "2", "8", "6", "5", "1", "9", "7"],
                    ["8", "9", "5", "4", "7", "1", "2", "3", "6"],
                    ["9", "4", "6", "2", "5", "7", "3", "1", "8"],
                    ["2", "1", "3", "6", "4", "8", "5", "7", "9"],
                    ["5", "8", "7", "1", "9", "3", "4", "6", "2"],
                    ["6", "5", "8", "7", "3", "4", "9", "2", "1"],
                    ["1", "7", "4", "9", "8", "2", "6", "5", "3"],
                    ["3", "2", "9", "5", "1", "6", "7", "8", "4"]
                ]
            """;
    }

    private String invalidHigherRowsBoard() {
        return """
                [
                    ["2", "1", "9", "4", "5", "6", "3", "8", "7"],
                    ["8", "5", "6", "3", "7", "9", "2", "4", "1"],
                    ["7", "3", "4", "2", "8", "1", "5", "6", "9"],
                    ["4", "2", "7", "1", "9", "8", "6", "3", "5"],
                    ["1", "6", "3", "7", "2", "5", "8", "9", "4"],
                    ["5", "9", "8", "6", "4", "3", "7", "1", "2"],
                    ["9", "7", "1", "8", "3", "2", "4", "5", "6"],
                    ["6", "8", "2", "5", "1", "4", "9", "7", "3"],
                    ["3", "4", "5", "9", "6", "7", "1", "2", "8"],
                    ["3", "4", "5", "9", "6", "7", "1", "2", "8"]
                ]
            """;
    }

    private String invalidLesserRowsBoard() {
        return """
                [
                    ["2", "1", "9", "4", "5", "6", "3", "8", "7"],
                    ["8", "5", "6", "3", "7", "9", "2", "4", "1"],
                    ["7", "3", "4", "2", "8", "1", "5", "6", "9"],
                    ["4", "2", "7", "1", "9", "8", "6", "3", "5"],
                    ["1", "6", "3", "7", "2", "5", "8", "9", "4"],
                    ["5", "9", "8", "6", "4", "3", "7", "1", "2"],
                    ["9", "7", "1", "8", "3", "2", "4", "5", "6"],
                    ["6", "8", "2", "5", "1", "4", "9", "7", "3"]
                ]
            """;
    }

    private String invalidHigherColumnsBoard() {
        return """
                [
                    ["2", "1", "9", "4", "5", "6", "3", "8", "7", "7"],
                    ["8", "5", "6", "3", "7", "9", "2", "4", "1", "7"],
                    ["7", "3", "4", "2", "8", "1", "5", "6", "9", "7"],
                    ["4", "2", "7", "1", "9", "8", "6", "3", "5", "7"],
                    ["1", "6", "3", "7", "2", "5", "8", "9", "4", "7"],
                    ["5", "9", "8", "6", "4", "3", "7", "1", "2", "7"],
                    ["9", "7", "1", "8", "3", "2", "4", "5", "6", "7"],
                    ["6", "8", "2", "5", "1", "4", "9", "7", "3", "7"],
                    ["3", "4", "5", "9", "6", "7", "1", "2", "8", "7"]
                ]
            """;
    }

    private String invalidLesserColumnsBoard() {
        return """
                [
                    ["2", "1", "9", "4", "5", "6", "3", "8"],
                    ["8", "5", "6", "3", "7", "9", "2", "4"],
                    ["7", "3", "4", "2", "8", "1", "5", "6"],
                    ["4", "2", "7", "1", "9", "8", "6", "3"],
                    ["1", "6", "3", "7", "2", "5", "8", "9"],
                    ["5", "9", "8", "6", "4", "3", "7", "1"],
                    ["9", "7", "1", "8", "3", "2", "4", "5"],
                    ["6", "8", "2", "5", "1", "4", "9", "7"],
                    ["3", "4", "5", "9", "6", "7", "1", "2"]
                ]
            """;
    }
}