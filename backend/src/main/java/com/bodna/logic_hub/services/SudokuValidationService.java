package com.bodna.logic_hub.services;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SudokuValidationService {

    @Autowired
    SudokuGenerationService sudokuGenerationService;

    public boolean isValidSolution(SudokuBoard solution, Long seed, Difficulty difficulty) {
        SudokuBoard seededBoard = sudokuGenerationService.generateBoard(seed, difficulty);

        return seededBoard.isEqual(solution);
    }
}
