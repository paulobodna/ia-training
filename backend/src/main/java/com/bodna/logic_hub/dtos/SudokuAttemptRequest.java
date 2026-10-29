package com.bodna.logic_hub.dtos;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.validation.ValidSudokuBoard;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public record SudokuAttemptRequest(

    @NotNull
    @Size(min = 9, max = 9)
    @ValidSudokuBoard
    String[][] solution,

    @NotNull
    Long seed,

    @NotNull @Positive
    byte generatorVersion,

    @NotNull
    Difficulty difficulty,

    @NotNull @Min(0) @Max(65)
    byte tipsUsed,

    @NotNull
    @PastOrPresent
    Instant completedAt,

    @NotNull
    Duration timeElapsed
) {

    public SudokuAttemptRequest {
        solution = deepCopy(solution);
    }

    private static String[][] deepCopy(String[][] solution) {
        return Arrays.stream(solution)
            .map(String[]::clone)
            .toArray(String[][]::new);
    }

    @Override
    public String[][] solution() {
        return deepCopy(solution);
    }
}
