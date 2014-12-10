package com.ndpar.utils

import org.junit.Test

class SortTest {

    @Test
    void testInsertionSort() {
        // array length < 47
        int[] array = [40, 5, 69, 46, 34, 40, 46, 84, 29, 8, 75, 97, 24]
        Arrays.sort(array)
    }

    @Test
    void testDualPivotQuicksort() {
        // array length < 286
        int[] array = [90, 47, 58, 29, 22, 32, 55, 5, 55, 73, 58, 50, 40, 5, 69, 46, 34, 40, 46, 84, 29, 8, 75, 97, 24,
                       40, 21, 82, 77, 9, 63, 92, 51, 92, 39, 15, 43, 89, 36, 69, 40, 16, 23, 2, 29, 91, 57, 43, 55, 22]
        Arrays.sort(array)
    }

    @Test
    void testDualPivotQuicksortLargsCenterPart() {
        // TODO
    }

    @Test
    void testDualPivotQuicksortEqualElements() {
        // TODO
    }
}
