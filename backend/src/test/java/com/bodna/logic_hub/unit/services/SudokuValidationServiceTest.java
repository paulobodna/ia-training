package com.bodna.logic_hub.unit.services;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.fixture.SudokuTestBoards;
import com.bodna.logic_hub.services.SudokuGenerationService;
import com.bodna.logic_hub.services.SudokuValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SudokuValidationServiceTest {

    @Mock
    SudokuGenerationService sudokuGenerationService;

    @InjectMocks
    private SudokuValidationService service;


    @Test
    void shouldAssertTrueForEqualBoard() {
        SudokuBoard solution = new SudokuBoard(SudokuTestBoards.validBoard());

        when(sudokuGenerationService.generateBoard(any(), any()))
            .thenReturn(new SudokuBoard(solution.getBoard()));

        assertTrue(service.isValidSolution(solution, 0L, Difficulty.values()[0]));
    }

    @Test
    void shouldAssertFalseForNotEqualBoard() {
        SudokuBoard solution = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());

        when(sudokuGenerationService.generateBoard(any(), any()))
            .thenReturn(new SudokuBoard(SudokuTestBoards.validBoard()));

        assertFalse(service.isValidSolution(solution, 0L, Difficulty.values()[0]));
    }
}