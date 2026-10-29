package com.bodna.logic_hub.unit.domain.sudoku;

import com.bodna.logic_hub.domain.sudoku.SudokuValidityChecker;
import com.bodna.logic_hub.fixture.SudokuTestBoards;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuValidityCheckerTest {

    @Test
    void stringMatrixBoardShouldBe9x9() {
        assertValidSize(
            SudokuValidityChecker::hasValidSize,
            new String[9][9],
            new String[8][9],
            new String[9][8]
        );
    }

    @Test
    void intMatrixBoardShouldBe9x9() {
        assertValidSize(
            SudokuValidityChecker::hasValidSize,
            new int[9][9],
            new int[8][9],
            new int[9][8]
        );
    }

    @Test
    void byteMatrixBoardShouldBe9x9() {
        assertValidSize(
            SudokuValidityChecker::hasValidSize,
            new byte[9][9],
            new byte[8][9],
            new byte[9][8]
        );
    }

    @Test
    void stringMatrixValuesShouldBeBetween0And9() {
        assertValidValues(
            SudokuValidityChecker::hasValidChars,
            SudokuTestBoards.stringValidBoard(),
            SudokuTestBoards.stringValidZeroedValuesBoard(),
            SudokuTestBoards.stringInvalidHighValuesBoard(),
            SudokuTestBoards.stringInvalidLowValuesBoard()
        );

        assertTrue(SudokuValidityChecker.hasValidChars(
            SudokuTestBoards.stringValidEmptyValuesBoard()
        ));

        assertFalse(SudokuValidityChecker.hasValidChars(
            SudokuTestBoards.stringPhraseInvalidBoard()
        ));

        assertFalse(SudokuValidityChecker.hasValidChars(
            SudokuTestBoards.stringInvalidLowCharBoard()
        ));

        assertFalse(SudokuValidityChecker.hasValidChars(
            SudokuTestBoards.stringInvalidHighCharBoard()
        ));
    }

    @Test
    void intMatrixValuesShouldBeBetween0And9() {
        assertValidValues(
            SudokuValidityChecker::hasValidNumbers,
            SudokuTestBoards.intValidBoard(),
            SudokuTestBoards.intValidZeroedValuesBoard(),
            SudokuTestBoards.intInvalidHighValuesBoard(),
            SudokuTestBoards.intInvalidLowValuesBoard()
        );
    }

    @Test
    void byteMatrixValuesShouldBeBetween0And9() {
        assertValidValues(
            SudokuValidityChecker::hasValidNumbers,
            SudokuTestBoards.validBoard(),
            SudokuTestBoards.validZeroedValuesBoard(),
            SudokuTestBoards.invalidHighValuesBoard(),
            SudokuTestBoards.invalidLowValuesBoard()
        );

    }

    private <T> void assertValidSize(
        Function<T, Boolean> checker,
        T validSizeBoard,
        T invalidRowsBoard,
        T invalidColumnsBoard
    ) {
        assertTrue(checker.apply(validSizeBoard));
        assertFalse(checker.apply(invalidRowsBoard));
        assertFalse(checker.apply(invalidColumnsBoard));
    }

    private <T> void assertValidValues(
        Function<T, Boolean> checker,
        T validValuesBoard,
        T validZeroedValuesBoard,
        T invalidHighValuesBoard,
        T invalidLowValuesBoard
    ) {
        assertTrue(checker.apply(validValuesBoard));
        assertTrue(checker.apply(validZeroedValuesBoard));
        assertFalse(checker.apply(invalidHighValuesBoard));
        assertFalse(checker.apply(invalidLowValuesBoard));
    }
}