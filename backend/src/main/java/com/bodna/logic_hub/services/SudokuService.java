package com.bodna.logic_hub.services;

import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.dtos.SudokuAttemptRequest;
import com.bodna.logic_hub.exceptions.InvalidSudokuSolutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SudokuService {

    @Autowired
    private SudokuValidationService sudokuValidationService;
    @Autowired
    private SudokuAttemptService sudokuAttemptService;


    public void submitSudokuAttempt(SudokuAttemptRequest attemptRequest) {
        SudokuBoard attemptSolution = new SudokuBoard(attemptRequest.solution());

        boolean isValid = sudokuValidationService.isValidSolution(
            attemptSolution,
            attemptRequest.seed(),
            attemptRequest.difficulty()
        );

        if (!isValid) {
            throw new InvalidSudokuSolutionException("The provided Sudoku solution is invalid");
        }

        sudokuAttemptService.finishAttempt(attemptRequest);
    }
}
