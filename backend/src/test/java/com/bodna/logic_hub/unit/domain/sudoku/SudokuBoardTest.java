package com.bodna.logic_hub.unit.domain.sudoku;

import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.exceptions.InvalidSudokuException;
import com.bodna.logic_hub.fixture.SudokuTestBoards;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuBoardTest {

    @Test
    void shouldCreateInstanceFromByteBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validBoard()));
    }

    @Test
    void shouldCreateInstanceFromIntBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.intValidBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validBoard()));
    }

    @Test
    void shouldCreateInstanceFromStringBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.stringValidBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validBoard()));
    }

    @Test
    void shouldCreateInstanceFromByteZeroedBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validZeroedValuesBoard()));
    }

    @Test
    void shouldCreateInstanceFromIntZeroedBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.intValidZeroedValuesBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validZeroedValuesBoard()));
    }

    @Test
    void shouldCreateInstanceFromStringZeroedBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.stringValidZeroedValuesBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validZeroedValuesBoard()));
    }

    @Test
    void shouldCreateInstanceFromStringEmptyBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.stringValidEmptyValuesBoard());
        assertTrue(Arrays.deepEquals(board.getBoard(), SudokuTestBoards.validZeroedValuesBoard()));
    }

    @Test
    void shouldThrowInvalidSudokuExceptionForInvalidSizeBoard() {
        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(new byte[8][9])
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(new byte[9][8])
        );
    }

    @Test
    void shouldThrowInvalidSudokuExceptionForIntInvalidSizeBoard() {
        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(new int[8][9])
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(new int[9][8])
        );
    }

    @Test
    void shouldThrowInvalidSudokuExceptionForStringInvalidSizeBoard() {
        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(new String[8][9])
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(new String[9][8])
        );
    }

    @Test
    void shouldThrowInvalidSudokuExceptionForInvalidValuesBoard() {
        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(SudokuTestBoards.invalidHighValuesBoard())
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(SudokuTestBoards.invalidLowValuesBoard())
        );
    }

    @Test
    void shouldThrowInvalidSudokuExceptionForIntInvalidValuesBoard() {
        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(SudokuTestBoards.intInvalidHighValuesBoard())
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(SudokuTestBoards.intInvalidLowValuesBoard())
        );
    }

    @Test
    void shouldThrowInvalidSudokuExceptionForStringInvalidValuesBoard() {
        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(SudokuTestBoards.stringInvalidHighValuesBoard())
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> new SudokuBoard(SudokuTestBoards.stringInvalidLowValuesBoard())
        );
    }

    @Test
    void shouldReturnRightCell() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validBoard());
        byte[][] targetBoard = SudokuTestBoards.validBoard();

        for (int row = 0; row < targetBoard.length; row++) {
            for (int col = 0; col < targetBoard[row].length; col++) {
                assertEquals(targetBoard[row][col], board.getCell(row, col));
            }
        }
    }

    @Test
    void shouldSetRightCell() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());
        byte[][] targetBoard = SudokuTestBoards.validBoard();

        for (int row = 0; row < targetBoard.length; row++) {
            for (int col = 0; col < targetBoard[row].length; col++) {
                board.setCell(row, col, targetBoard[row][col]);
            }
        }

        assertTrue(Arrays.deepEquals(board.getBoard(), targetBoard));
    }

    @Test
    void shouldThrowInvalidSudokuExceptionWhenSettingInvalidValueToCell() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());

        assertThrows(
            InvalidSudokuException.class,
            () -> board.setCell(0, 0, (byte) 10)
        );

        assertThrows(
            InvalidSudokuException.class,
            () -> board.setCell(0, 0, (byte) -1)
        );
    }

    @Test
    void shouldCheckTrueForValidBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validBoard());
        assertTrue(board.isValid());
    }

    @Test
    void shouldCheckFalseForInvalidBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.invalidBoard());
        assertFalse(board.isValid());
    }

    @Test
    void shouldCheckTrueForCompleteBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validBoard());
        assertTrue(board.isComplete());
    }

    @Test
    void shouldCheckFalseForIncompleteBoard() {
        SudokuBoard board = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());
        assertFalse(board.isComplete());
    }

    @Test
    void shouldCreateBoardCopy() {
        byte[][] targetBoard = SudokuTestBoards.validBoard();
        SudokuBoard board = new SudokuBoard(targetBoard);

        assertArrayEquals(board.copyBoard().getBoard(), targetBoard);
    }

    @Test
    void shouldAssertTrueForEqualBoard() {
        SudokuBoard sourceBoard = new SudokuBoard(SudokuTestBoards.validBoard());
        SudokuBoard targetBoard = new SudokuBoard(SudokuTestBoards.validBoard());

        assertTrue(sourceBoard.isEqual(targetBoard));
    }

    @Test
    void shouldAssertFalseForInequalBoard() {
        SudokuBoard sourceBoard = new SudokuBoard(SudokuTestBoards.validBoard());
        SudokuBoard targetBoard = new SudokuBoard(SudokuTestBoards.validZeroedValuesBoard());

        assertFalse(sourceBoard.isEqual(targetBoard));
    }
}