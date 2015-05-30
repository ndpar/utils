package com.ndpar.math

import static java.math.BigInteger.ONE

class Math {

    static BigInteger sqrt(BigInteger n) {
        def a = ONE
        def b = n.shiftRight(5) + 8
        while (b >= a) {
            def mid = (a + b).shiftRight(1)
            if (mid * mid > n) b = mid - ONE
            else a = mid + ONE
        }
        a - ONE
    }
}
