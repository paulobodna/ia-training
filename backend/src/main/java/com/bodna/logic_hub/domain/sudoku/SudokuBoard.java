package com.bodna.logic_hub.domain.sudoku;

import com.bodna.logic_hub.exceptions.InvalidSudokuException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
public final class SudokuBoard {

    private static final String VALIDITY_VALUE_ERROR_MESSAGE = "Sudoku values must be between 1 and 9";
    private static final String VALIDITY_SIZE_ERROR_MESSAGE = "Sudoku board size must be 9x9";
    @Getter
    private final byte[][] board;

    public SudokuBoard(byte[][] newBoard) {
        if (!SudokuValidityChecker.hasValidSize(newBoard)) {
            throw new InvalidSudokuException(VALIDITY_SIZE_ERROR_MESSAGE);
        } else if (!SudokuValidityChecker.hasValidNumbers(newBoard)) {
            throw new InvalidSudokuException(VALIDITY_VALUE_ERROR_MESSAGE);
        } else {
            this.board = deepCopy(newBoard);
        }
    }

    public SudokuBoard(String[][] newBoard) {
        if (!SudokuValidityChecker.hasValidSize(newBoard)) {
            throw new InvalidSudokuException(VALIDITY_SIZE_ERROR_MESSAGE);
        } else if (!SudokuValidityChecker.hasValidChars(newBoard)) {
            throw new InvalidSudokuException(VALIDITY_VALUE_ERROR_MESSAGE);
        } else {
            this.board = this.toByteBoardFromString(newBoard);
        }
    }

    public SudokuBoard(int[][] newBoard) {
        if (!SudokuValidityChecker.hasValidSize(newBoard)) {
            throw new InvalidSudokuException(VALIDITY_SIZE_ERROR_MESSAGE);
        } else if (!SudokuValidityChecker.hasValidNumbers(newBoard)) {
            throw new InvalidSudokuException(VALIDITY_VALUE_ERROR_MESSAGE);
        } else {
            this.board = this.toByteBoardFromInt(newBoard);
        }
    }

    public byte getCell(int row, int col) {
        return this.board[row][col];
    }

    public void setCell(int row, int col, byte value) {
        if (this.isValidNumber(value)) {
            this.board[row][col] = value;
        } else {
            throw new InvalidSudokuException(VALIDITY_VALUE_ERROR_MESSAGE);
        }
    }

    public boolean isValid() {
        return this.hasRowsUnicity();
    }

    public boolean isComplete() {
        for (byte[] row : this.board) {
            for (int value : row) {
                if (value == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public SudokuBoard copyBoard() {
        byte[][] copy = new byte[board.length][];

        for (int i = 0; i < board.length; i++) {
            copy[i] = board[i].clone();
        }

        return new SudokuBoard(copy);
    }

    public boolean isEqual(SudokuBoard target) {
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                if (this.getCell(row, col) != target.getCell(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    public void printToFile() throws IOException {
        Path path = Paths.get("target/generated-sudoku.txt");

        StringBuilder content = new StringBuilder();

        for (byte[] row : this.board) {
            for (byte value : row) {
                content.append(value).append(" ");
            }
            content.append("\n");
        }

        Files.writeString(path, content);
    }
    */

    private byte[][] deepCopy(byte[][] source) {
        byte[][] copy = new byte[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = Arrays.copyOf(source[i], source[i].length);
        }

        return copy;
    }

    private byte @NonNull [][] toByteBoardFromString(String @NonNull [][] board) {
        byte[][] result = new byte[board.length][board[0].length];

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                String value = board[row][col];

                result[row][col] = value.isEmpty()
                    ? 0
                    : Byte.parseByte(value);
            }
        }

        return result;
    }

    private byte @NonNull [][] toByteBoardFromInt(int @NonNull [][] board) {
        byte[][] result = new byte[board.length][];

        for (int row = 0; row < board.length; row++) {
            result[row] = new byte[board[row].length];

            for (int col = 0; col < board[row].length; col++) {
                result[row][col] = (byte) board[row][col];
            }
        }

        return result;
    }

    private boolean hasRowsUnicity() {
        for (byte[] row : this.board) {
            HashSet<Byte> rowSet = new HashSet<>();

            for (byte value : row) {
                rowSet.add(value);
            }

            if (rowSet.size() != 9) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidNumber(byte number) {
        return number >= 0 && number < 10;
    }

}
