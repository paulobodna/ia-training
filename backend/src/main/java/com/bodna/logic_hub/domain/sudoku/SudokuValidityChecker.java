package com.bodna.logic_hub.domain.sudoku;


public final class SudokuValidityChecker {

    public static boolean hasValidSize(String[][] board) {
        if (board.length != 9) {
            return false;
        }

        for (String[] row : board) {
            if (row.length != 9) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasValidSize(int[][] board) {
        if (board.length != 9) {
            return false;
        }

        for (int[] row : board) {
            if (row.length != 9) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasValidSize(byte[][] board) {
        if (board.length != 9) {
            return false;
        }

        for (byte[] row : board) {
            if (row.length != 9) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasValidChars(String[][] board) {
        for (String[] row : board) {
            for (String value : row) {
                if (value.length() > 1
                    || (!value.isEmpty() && value.charAt(0) < '0')
                    || (!value.isEmpty() && value.charAt(0) > '9')
                ) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean hasValidNumbers(int[][] board) {
        for (int[] row : board) {
            for (int value : row) {
                if (value < 0 || value > 9) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean hasValidNumbers(byte[][] board) {
        for (byte[] row : board) {
            for (int value : row) {
                if (value < 0 || value > 9) {
                    return false;
                }
            }
        }

        return true;
    }
}
