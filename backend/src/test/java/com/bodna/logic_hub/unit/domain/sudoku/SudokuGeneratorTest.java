package com.bodna.logic_hub.unit.domain.sudoku;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.domain.sudoku.SudokuGenerator;
import com.bodna.logic_hub.exceptions.InvalidSudokuException;
import com.bodna.logic_hub.fixture.SudokuTestBoards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuGeneratorTest {

    private List<SudokuBoard> sudokuBoards;

    @BeforeEach
    void setUp() {
        sudokuBoards = List.of(
            new SudokuBoard(new byte[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
            }),
            new SudokuBoard(new byte[][]{
                {7, 3, 8, 1, 9, 5, 6, 2, 4},
                {2, 6, 5, 8, 4, 7, 3, 9, 1},
                {1, 4, 9, 2, 3, 6, 8, 5, 7},
                {3, 8, 2, 9, 1, 4, 7, 6, 5},
                {4, 5, 6, 7, 8, 2, 1, 3, 9},
                {9, 7, 1, 5, 6, 3, 4, 8, 2},
                {6, 9, 7, 3, 5, 1, 2, 4, 8},
                {8, 2, 3, 4, 7, 9, 5, 1, 6},
                {5, 1, 4, 6, 2, 8, 9, 7, 3}
            }),
            new SudokuBoard(new byte[][]{
                {8, 4, 2, 6, 1, 3, 7, 9, 5},
                {6, 5, 7, 4, 8, 9, 2, 3, 1},
                {3, 1, 9, 5, 2, 7, 6, 8, 4},
                {7, 3, 1, 2, 4, 8, 9, 5, 6},
                {9, 8, 5, 3, 7, 6, 4, 1, 2},
                {4, 2, 6, 9, 5, 1, 8, 7, 3},
                {2, 7, 8, 1, 3, 4, 5, 6, 9},
                {5, 9, 3, 8, 6, 2, 1, 4, 7},
                {1, 6, 4, 7, 9, 5, 3, 2, 8}
            }),
            new SudokuBoard(new byte[][]{
                {9, 4, 1, 5, 2, 7, 6, 3, 8},
                {6, 3, 7, 1, 8, 4, 9, 5, 2},
                {2, 5, 8, 3, 9, 6, 4, 7, 1},
                {5, 2, 9, 4, 6, 3, 8, 1, 7},
                {7, 1, 3, 8, 5, 9, 2, 4, 6},
                {4, 8, 6, 2, 7, 1, 5, 9, 3},
                {8, 6, 4, 7, 3, 5, 1, 2, 9},
                {1, 7, 2, 9, 4, 8, 3, 6, 5},
                {3, 9, 5, 6, 1, 2, 7, 8, 4}
            })
        );
    }


    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shouldGenerateSameBoardForSameRandomizer(long seed) {
        SudokuBoard board1 = SudokuGenerator.generateBoard(
            sudokuBoards, seed, Difficulty.values()[0]
        );

        SudokuBoard board2 = SudokuGenerator.generateBoard(
            sudokuBoards, seed, Difficulty.values()[0]
        );

        assertTrue(board1.isEqual(board2));
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shouldGenerateDifferentBoardForDifferentRandomizer(long seed) {
        SudokuBoard board1 = SudokuGenerator.generateBoard(
            sudokuBoards, seed, Difficulty.values()[0]
        );

        SudokuBoard board2 = SudokuGenerator.generateBoard(
            sudokuBoards, seed + 1, Difficulty.values()[0]
        );

        assertFalse(board1.isEqual(board2));
    }

    @Test
    void shouldThrowInvalidSudokuExceptionWhenGeneratedBoardIsIncomplete() {
        List<SudokuBoard> incompleteList = List.of(new SudokuBoard(
            SudokuTestBoards.validZeroedValuesBoard()
        ));

        assertThrows(
            InvalidSudokuException.class,
            () -> SudokuGenerator.generateBoard(
                incompleteList, 0L, Difficulty.values()[0]
            )
        );
    }

    @Test
    void shouldThrowInvalidSudokuExceptionWhenGeneratedBoardIsInvalid() {
        List<SudokuBoard> invalidList = List.of(new SudokuBoard(
            SudokuTestBoards.invalidBoard()
        ));

        assertThrows(
            InvalidSudokuException.class,
            () -> SudokuGenerator.generateBoard(
                invalidList, 0L, Difficulty.values()[0]
            )
        );
    }
}