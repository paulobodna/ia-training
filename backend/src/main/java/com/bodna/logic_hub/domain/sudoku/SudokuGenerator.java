package com.bodna.logic_hub.domain.sudoku;

import com.bodna.logic_hub.exceptions.InvalidSudokuException;
import com.bodna.logic_hub.utils.SplitMix64;

import java.util.List;


public final class SudokuGenerator {

    public static SudokuBoard generateBoard(List<SudokuBoard> baseBoards, Long seed, Difficulty difficulty) {
        SplitMix64 random = new SplitMix64(seed, difficulty);
        int randomBaseIndex = (int) Long.remainderUnsigned(random.nextLong(), baseBoards.size());
        SudokuBoard board = baseBoards.get(randomBaseIndex).copyBoard();

        SudokuShuffler.shuffle(board, random);

        if (!board.isComplete() || !board.isValid()) {
            throw new InvalidSudokuException("Invalid or incompleted Sudoku generated board");
        }

        return board;
    }
}