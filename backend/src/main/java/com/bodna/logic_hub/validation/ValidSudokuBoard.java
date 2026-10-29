package com.bodna.logic_hub.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SudokuBoardValidator.class)
public @interface ValidSudokuBoard {

    String message() default "Invalid Sudoku board";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
