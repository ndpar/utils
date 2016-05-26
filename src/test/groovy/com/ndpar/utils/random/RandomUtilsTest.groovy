package com.ndpar.utils.random

import org.junit.Test

class RandomUtilsTest {

    @Test
    void draw_all() {
        assert RandomUtils.draw(5, 5) == 0..<5 as long[]
    }

    @Test
    void draw_small() {
        def result = RandomUtils.draw(5, 10) as List
        println result
        assert result == result.sort()
    }

    @Test
    void draw_large() {
        def result = RandomUtils.draw(5, 1000) as List
        println result
        assert result == result.sort()
    }
}
