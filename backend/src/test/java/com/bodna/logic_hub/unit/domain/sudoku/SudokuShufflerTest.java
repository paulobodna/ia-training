package com.bodna.logic_hub.unit.domain.sudoku;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.domain.sudoku.SudokuShuffler;
import com.bodna.logic_hub.domain.sudoku.SudokuValidityChecker;
import com.bodna.logic_hub.exceptions.InvalidSudokuException;
import com.bodna.logic_hub.fixture.SudokuTestBoards;
import com.bodna.logic_hub.utils.SplitMix64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuShufflerTest {

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shuffleShouldProduceValidSudokuFromValidBoard(long seed) {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validBoard());
        SplitMix64 random = new SplitMix64(seed, Difficulty.values()[0]);

        SudokuShuffler.shuffle(board, random);

        Assertions.assertTrue(SudokuValidityChecker.hasValidSize(board.getBoard()));
        assertTrue(SudokuValidityChecker.hasValidNumbers(board.getBoard()));
        assertTrue(board.isComplete());
        assertTrue(board.isValid());
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shuffleShouldProduceSameBoardForSameRandomizer(long seed) {
        SudokuBoard board1 = new SudokuBoard(SudokuTestBoards.validBoard());
        SplitMix64 random = new SplitMix64(seed, Difficulty.values()[0]);

        SudokuShuffler.shuffle(board1, random);

        SudokuBoard board2 = new SudokuBoard(SudokuTestBoards.validBoard());
        random = new SplitMix64(seed, Difficulty.values()[0]);

        SudokuShuffler.shuffle(board2, random);

        assertTrue(board1.isEqual(board2));
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shuffleShouldProduceDifferentBoardForDifferentRandomizer(long seed) {
        SudokuBoard board1 = new SudokuBoard(SudokuTestBoards.validBoard());
        SplitMix64 random = new SplitMix64(seed, Difficulty.values()[0]);

        SudokuShuffler.shuffle(board1, random);

        SudokuBoard board2 = new SudokuBoard(SudokuTestBoards.validBoard());
        random = new SplitMix64(seed + 1, Difficulty.values()[0]);

        SudokuShuffler.shuffle(board2, random);

        assertFalse(board1.isEqual(board2));
    }

    @Test
    void shuffleShouldProduceInvalidSudokuFromInvalidBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.invalidBoard());
        SplitMix64 random = new SplitMix64(0L, Difficulty.values()[0]);

        SudokuShuffler.shuffle(board, random);

        assertTrue(board.isComplete());
        assertFalse(board.isValid());
    }

    @Test
    void shouldThrowInvalidSudokuExceptionWhenBoardIsIncomplete() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());
        SplitMix64 random = new SplitMix64(0L, Difficulty.values()[0]);

        assertThrows(
            InvalidSudokuException.class,
            () -> SudokuShuffler.shuffle(board, random)
        );
    }
}