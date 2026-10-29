package com.bodna.logic_hub.utils;

import com.bodna.logic_hub.domain.sudoku.Difficulty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SplitMix64 {
    private long state;
    private byte difficulty;

    public SplitMix64(long state, Difficulty difficulty) {
        this.state = state;
        this.difficulty = (byte) difficulty.ordinal();
    }

    public long nextLong() {
        long z = state += 0x9E3779B97F4A7C15L; // Incremento baseado na proporção áurea
        z ^= ((long) difficulty << 24) ^ 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 30)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }
}