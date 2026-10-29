package com.bodna.logic_hub.utils;


public final class FisherYates {
    public static void shuffle(byte[] array, SplitMix64 random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) Long.remainderUnsigned(random.nextLong(), i + 1);

            byte temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
