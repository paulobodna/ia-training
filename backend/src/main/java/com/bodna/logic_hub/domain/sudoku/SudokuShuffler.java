package com.bodna.logic_hub.domain.sudoku;

import com.bodna.logic_hub.exceptions.InvalidSudokuException;
import com.bodna.logic_hub.utils.FisherYates;
import com.bodna.logic_hub.utils.SplitMix64;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public final class SudokuShuffler {

    public static void shuffle(SudokuBoard board, SplitMix64 random) {
        if (!board.isComplete()) {
            throw new InvalidSudokuException("Can't shuffle incomplete board.");
        }

        shuffleNumbers(board, random);
        shuffleRows(board, random);
        shuffleColumns(board, random);
        shuffleBands(board, random);
        shuffleStacks(board, random);
    }


    private static void shuffleNumbers(SudokuBoard board, SplitMix64 random) {
        byte[] numbers = createSequentialArray(1, 9);
        FisherYates.shuffle(numbers, random);
        log.debug("shuffled numbers: {}", Arrays.toString(numbers));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                byte value = board.getCell(i, j);
                board.setCell(i, j, numbers[value - 1]);
            }
        }
    }

    private static void shuffleRows(SudokuBoard board, SplitMix64 random) {
        byte[] firstBandRows = createSequentialArray(0, 2);
        byte[] secondBandRows = createSequentialArray(3, 5);
        byte[] thirdBandRows = createSequentialArray(6, 8);

        FisherYates.shuffle(firstBandRows, random);
        FisherYates.shuffle(secondBandRows, random);
        FisherYates.shuffle(thirdBandRows, random);

        SudokuBoard boardCopy = board.copyBoard();
        byte[] shuffledRows = concatArrays(firstBandRows, secondBandRows, thirdBandRows);
        log.debug("shuffled rows: {}", Arrays.toString(shuffledRows));

        for (int row = 0; row < shuffledRows.length; row++) {
            for (int col = 0; col < shuffledRows.length; col++) {
                board.setCell(row, col, boardCopy.getCell(shuffledRows[row], col));
            }
        }
    }

    private static void shuffleColumns(SudokuBoard board, SplitMix64 random) {
        byte[] firstStackColumns = createSequentialArray(0, 2);
        byte[] secondStackColumns = createSequentialArray(3, 5);
        byte[] thirdStackColumns = createSequentialArray(6, 8);

        FisherYates.shuffle(firstStackColumns, random);
        FisherYates.shuffle(secondStackColumns, random);
        FisherYates.shuffle(thirdStackColumns, random);

        SudokuBoard boardCopy = board.copyBoard();
        byte[] shuffledColumns = concatArrays(firstStackColumns, secondStackColumns, thirdStackColumns);
        log.debug("shuffled columns: {}", Arrays.toString(shuffledColumns));

        for (int col = 0; col < shuffledColumns.length; col++) {
            for (int row = 0; row < shuffledColumns.length; row++) {
                board.setCell(row, col, boardCopy.getCell(row, shuffledColumns[col]));
            }
        }
    }

    private static void shuffleBands(SudokuBoard board, SplitMix64 random) {
        byte[] bands = createSequentialArray(0, 2);

        FisherYates.shuffle(bands, random);
        log.debug("shuffled bands: {}", Arrays.toString(bands));

        SudokuBoard boardCopy = board.copyBoard();

        for (int i = 0; i < 9; i += 3) {
            int startRow = bands[i / 3] * 3;
            for (int col = 0; col < 9; col++) {
                board.setCell(i, col, boardCopy.getCell(startRow, col));
                board.setCell(i + 1, col, boardCopy.getCell(startRow + 1, col));
                board.setCell(i + 2, col, boardCopy.getCell(startRow + 2, col));
            }
        }
    }

    private static void shuffleStacks(SudokuBoard board, SplitMix64 random) {
        byte[] stacks = createSequentialArray(0, 2);

        FisherYates.shuffle(stacks, random);
        log.debug("shuffled stacks: {}", Arrays.toString(stacks));

        SudokuBoard boardCopy = board.copyBoard();

        for (int i = 0; i < 9; i += 3) {
            int startColumn = stacks[i / 3] * 3;
            for (int row = 0; row < 9; row++) {
                board.setCell(row, i, boardCopy.getCell(row, startColumn));
                board.setCell(row, i + 1, boardCopy.getCell(row, startColumn + 1));
                board.setCell(row, i + 2, boardCopy.getCell(row, startColumn + 2));
            }
        }
    }


    private static byte[] concatArrays(byte[] first, byte[] second, byte[] third) {
        byte[] result = new byte[first.length + second.length + third.length];

        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        System.arraycopy(third, 0, result, first.length + second.length, third.length);

        return result;
    }

    private static byte[] createSequentialArray(int begin, int end) {
        int arraySize = end - begin + 1;
        byte[] array = new byte[arraySize];
        for (int i = 0, j = begin; i < arraySize; i++, j++) {
            array[i] = (byte) j;
        }
        return array;
    }
}