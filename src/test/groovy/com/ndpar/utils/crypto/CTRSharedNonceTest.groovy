package com.ndpar.utils.crypto

import org.junit.Test

import static com.ndpar.utils.crypto.HexStrings.*

class CTRSharedNonceTest {

    @Test
    void do_not_reuse_nonce_in_ctr_mode() {
        // plain text
        def p = '43 72 79 70 74 6F 67 72 61 70 68 79 20 43 72 79.70 74 6F 67 72 61 70 68 79 20 43 72 79 70 74 6F'
        assert toAscii(p) == 'Cryptography Cryptography Crypto'

        // ciphertext produced from p by CTR mode
        def c = '46 64 DC 06 97 BB FE 69 33 07 15 07 9B A6 C2 3D.2B 84 DE 4F 90 8D 7D 34 AA CE 96 8B 64 F3 DF 75'

        // ciphertext produced from another text (P) by CTR mode with the same nonce
        def C = '51 7E CC 05 C3 BD EA 3B 33 57 0E 1B D8 97 D5 30.7B D0 91 6B 8D 82 6B 35 B7 8B BB 8D 74 E2 C7 3B'

        // we can now reveal the second text
        def P = xor(p, c, C)
        println P
        assert toAscii(P) == 'This is a secret   Confidential!'
    }
}
