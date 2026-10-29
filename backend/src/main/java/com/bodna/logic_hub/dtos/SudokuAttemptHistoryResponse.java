package com.bodna.logic_hub.dtos;

import com.bodna.logic_hub.domain.sudoku.Difficulty;

import java.time.Duration;
import java.time.Instant;

public record SudokuAttemptHistoryResponse(
    Long seed,
    byte generatorVersion,
    Difficulty difficulty,
    byte tipsUsed,
    Instant completedAt,
    Duration timeElapsed,
    short xpObtained
) {
}
