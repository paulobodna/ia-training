package com.bodna.logic_hub.unit.utils;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.utils.SplitMix64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class SplitMix64Test {

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shouldAlwaysReturnSameValuesForSameSeedAndDifficulty(long seed) {
        for (Difficulty difficulty : Difficulty.values()) {
            SplitMix64 random = new SplitMix64(seed, difficulty);

            long[] sourceValues = {random.nextLong(), random.nextLong(), random.nextLong()};

            random = new SplitMix64(seed, difficulty);

            long[] targetValues = {random.nextLong(), random.nextLong(), random.nextLong()};

            assertArrayEquals(sourceValues, targetValues);
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shouldReturnDifferentValuesForDifferentSeeds(long seed) {
        SplitMix64 random = new SplitMix64(seed, Difficulty.values()[0]);

        long[] sourceValues = {random.nextLong(), random.nextLong(), random.nextLong()};

        random = new SplitMix64(seed + 1, Difficulty.values()[0]);

        long[] targetValues = {random.nextLong(), random.nextLong(), random.nextLong()};

        assertFalse(Arrays.equals(sourceValues, targetValues));
    }

    @Test
    void shouldReturnDifferentValuesForDifferentDifficulties() {
        for (Difficulty sourceDifficulty : Difficulty.values()) {
            SplitMix64 random = new SplitMix64(0, sourceDifficulty);

            long[] sourceValues = {random.nextLong(), random.nextLong(), random.nextLong()};

            Difficulty targetDifficulty = sourceDifficulty.ordinal() == Difficulty.values()[
                Difficulty.values().length - 1
                ].ordinal()
                ? Difficulty.values()[0]
                : Difficulty.values()[sourceDifficulty.ordinal() + 1];

            random = new SplitMix64(0, targetDifficulty);

            long[] targetValues = {random.nextLong(), random.nextLong(), random.nextLong()};

            assertFalse(Arrays.equals(sourceValues, targetValues));
        }
    }
}