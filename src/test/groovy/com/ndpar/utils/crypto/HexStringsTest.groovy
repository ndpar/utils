package com.ndpar.utils.crypto

import org.junit.Test

import static com.ndpar.utils.crypto.HexStrings.*

class HexStringsTest {

    @Test
    void xor() {
        assert '3306161706' == xor('43 72 79 70 74', '70.74.6F.67.72')
    }
}
