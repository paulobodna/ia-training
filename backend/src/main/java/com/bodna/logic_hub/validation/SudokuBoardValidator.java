package com.bodna.logic_hub.validation;

import com.bodna.logic_hub.domain.sudoku.SudokuValidityChecker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SudokuBoardValidator implements ConstraintValidator<ValidSudokuBoard, String[][]> {

    @Override
    public boolean isValid(
        String[][] board,
        ConstraintValidatorContext context
    ) {
        if (board == null) {
            return true;
        }

        return SudokuValidityChecker.hasValidSize(board) && SudokuValidityChecker.hasValidChars(board);
    }
}
