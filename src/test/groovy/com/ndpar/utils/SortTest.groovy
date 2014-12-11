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
        assert array == [
                2, 5, 5, 8, 9, 15, 16, 21, 22, 22, 23, 24, 29, 29, 29, 32, 34, 36, 39, 40, 40, 40, 40, 43, 43,
                46, 46, 47, 50, 51, 55, 55, 55, 57, 58, 58, 63, 69, 69, 73, 75, 77, 82, 84, 89, 90, 91, 92, 92, 97]
    }

    @Test
    void testDualPivotQuicksortLargeCenterPart() {
        int[] array = [90, 47, 58, 29, 42, 32, 55, 55, 55, 43, 58, 50, 40, 36, 59, 46, 44, 40, 46, 44, 29, 68, 75, 97, 24,
                       40, 58, 42, 37, 49, 43, 92, 51, 92, 39, 15, 43, 39, 36, 69, 40, 16, 23, 42, 39, 91, 57, 43, 55, 22]
        Arrays.sort(array)
        assert false
    }

    @Test
    void testDualPivotQuicksortEqualElements() {
        // TODO
    }
}
