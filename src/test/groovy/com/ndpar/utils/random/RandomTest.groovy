package com.ndpar.utils.random

import clojure.java.api.Clojure
import org.junit.BeforeClass
import org.junit.Test

/**
 * Demo: calling Clojure from Groovy (Java)
 */
class RandomTest {

    @BeforeClass
    static void setUpClass() {
        def require = Clojure.var('clojure.core', 'require')
        require.invoke(Clojure.read('com.ndpar.utils.random'))
    }

    @Test
    void test() {
        def draw = Clojure.var('com.ndpar.utils.random', 'draw')
        long[] result = draw.invoke(3, 5)

        println result
        assert result.size() == 3
    }
}
