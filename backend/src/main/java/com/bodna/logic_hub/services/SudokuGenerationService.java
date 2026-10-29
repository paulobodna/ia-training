package com.bodna.logic_hub.services;


import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.domain.sudoku.SudokuGenerator;
import com.bodna.logic_hub.infra.BaseSudokuBoardProvider;
import org.springframework.stereotype.Service;

@Service
public class SudokuGenerationService {

    private final BaseSudokuBoardProvider baseBoardProvider;

    public SudokuGenerationService(BaseSudokuBoardProvider baseBoardProvider) {
        this.baseBoardProvider = baseBoardProvider;
    }

    public SudokuBoard generateBoard(Long seed, Difficulty difficulty) {
        return SudokuGenerator.generateBoard(
            baseBoardProvider.getBaseSudokuBoards(),
            seed,
            difficulty
        );
    }
}
