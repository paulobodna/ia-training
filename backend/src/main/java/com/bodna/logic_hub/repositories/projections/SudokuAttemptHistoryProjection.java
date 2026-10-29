package com.bodna.logic_hub.repositories.projections;

import com.bodna.logic_hub.domain.sudoku.Difficulty;

import java.time.Duration;
import java.time.Instant;

public interface SudokuAttemptHistoryProjection {
    Long getSeed();

    byte getGeneratorVersion();

    Difficulty getDifficulty();

    byte getTipsUsed();

    Instant getCompletedAt();

    Duration getTimeElapsed();

    short getXpObtained();
}
