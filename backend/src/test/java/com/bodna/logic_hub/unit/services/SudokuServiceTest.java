package com.bodna.logic_hub.unit.services;

import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.dtos.SudokuAttemptRequest;
import com.bodna.logic_hub.exceptions.InvalidSudokuSolutionException;
import com.bodna.logic_hub.services.SudokuAttemptService;
import com.bodna.logic_hub.services.SudokuService;
import com.bodna.logic_hub.services.SudokuValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SudokuServiceTest {

    @Mock
    private SudokuValidationService sudokuValidationService;

    @Mock
    private SudokuAttemptService sudokuAttemptService;

    @InjectMocks
    private SudokuService sudokuService;


    @Test
    void shouldCallFinishAttemptWhenSolutionIsValid() {
        SudokuAttemptRequest request = createValidRequest();

        when(sudokuValidationService.isValidSolution(
            any(SudokuBoard.class),
            eq(request.seed()),
            eq(request.difficulty())
        )).thenReturn(true);

        sudokuService.submitSudokuAttempt(request);

        verify(sudokuValidationService).isValidSolution(
            any(SudokuBoard.class),
            eq(request.seed()),
            eq(request.difficulty())
        );

        verify(sudokuAttemptService).finishAttempt(request);
    }

    @Test
    void shouldThrowExceptionWhenSolutionIsInvalid() {
        SudokuAttemptRequest request = createInvalidRequest();

        when(sudokuValidationService.isValidSolution(
            any(SudokuBoard.class),
            eq(request.seed()),
            eq(request.difficulty())
        )).thenReturn(false);

        assertThrows(
            InvalidSudokuSolutionException.class,
            () -> sudokuService.submitSudokuAttempt(request)
        );

        verify(sudokuAttemptService, never()).finishAttempt(any());
    }


    private SudokuAttemptRequest createValidRequest() {
        String json = validRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(json, SudokuAttemptRequest.class);
    }

    private SudokuAttemptRequest createInvalidRequest() {
        String json = invalidRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(json, SudokuAttemptRequest.class);
    }

    private String validRequest() {
        return """
            {
              "solution": [
                ["2", "1", "9", "4", "5", "6", "3", "8", "7"],
                ["8", "5", "6", "3", "7", "9", "2", "4", "1"],
                ["7", "3", "4", "2", "8", "1", "5", "6", "9"],
                ["4", "2", "7", "1", "9", "8", "6", "3", "5"],
                ["1", "6", "3", "7", "2", "5", "8", "9", "4"],
                ["5", "9", "8", "6", "4", "3", "7", "1", "2"],
                ["9", "7", "1", "8", "3", "2", "4", "5", "6"],
                ["6", "8", "2", "5", "1", "4", "9", "7", "3"],
                ["3", "4", "5", "9", "6", "7", "1", "2", "8"]
              ],
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S",
              "userId": 1
            }
            """;
    }

    private String invalidRequest() {
        return """
            {
              "solution": [
                ["1", "1", "9", "4", "5", "6", "3", "8", "7"],
                ["8", "5", "6", "3", "7", "9", "2", "4", "1"],
                ["7", "3", "4", "2", "8", "1", "5", "6", "9"],
                ["4", "2", "7", "1", "9", "8", "6", "3", "5"],
                ["1", "6", "3", "7", "2", "5", "8", "9", "4"],
                ["5", "9", "8", "6", "4", "3", "7", "1", "2"],
                ["9", "7", "1", "8", "6", "4", "5", "2", "3"],
                ["3", "4", "5", "9", "1", "2", "8", "7", "6"],
                ["6", "8", "2", "5", "3", "7", "9", "4", "1"]
              ],
              "seed": 123456,
              "generatorVersion": 1,
              "difficulty": 1,
              "tipsUsed": 0,
              "completedAt": "2026-08-20T10:30:00-03:00",
              "timeElapsed": "PT30M5S",
              "userId": 1
            }
            """;
    }
}