package com.bodna.logic_hub.unit.utils;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import com.bodna.logic_hub.utils.FisherYates;
import com.bodna.logic_hub.utils.SplitMix64;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FisherYatesTest {

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shouldAlwaysReturnSameArrayForSameRandomizer(long seed) {
        SplitMix64 random = new SplitMix64(seed, Difficulty.values()[0]);
        byte[] firstArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FisherYates.shuffle(firstArray, random);

        random = new SplitMix64(seed, Difficulty.EASY);
        byte[] secondArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FisherYates.shuffle(secondArray, random);

        assertArrayEquals(firstArray, secondArray);
    }

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 1, 2, 100, 1000000})
    void shouldReturnDifferentArrayForDifferentRandomizer(long seed) {
        SplitMix64 random = new SplitMix64(seed, Difficulty.values()[0]);
        byte[] sourceArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FisherYates.shuffle(sourceArray, random);

        random = new SplitMix64(seed + 1, Difficulty.EASY);
        byte[] targetArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FisherYates.shuffle(targetArray, random);

        assertFalse(Arrays.equals(sourceArray, targetArray));
    }
}