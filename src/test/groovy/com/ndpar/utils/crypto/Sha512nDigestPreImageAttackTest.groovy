package com.ndpar.utils.crypto

import org.junit.Test

class Sha512nDigestPreImageAttackTest {

    /**
     * 22 ms
     */
    @Test
    void pre_image_attack_sha_512_8() {
        preImageAttack(8, 'A9')
        def digest = new Sha512nDigest(8)
        assert digest.digest('200') == 'A9'
    }

    /**
     * 633 ms
     */
    @Test
    void pre_image_attack_sha_512_16() {
//        preImageAttack(16, '3D4B')
        def digest = new Sha512nDigest(16)
        assert digest.digest('91199') == '3D4B'
    }

    /**
     * 2 min (118651 ms)
     */
    @Test
    void pre_image_attack_sha_512_24() {
//        preImageAttack(24, '3A7F27')
        def digest = new Sha512nDigest(24)
        assert digest.digest('10462227') == '3A7F27'
    }

    /**
     * 3 hours (10063815 ms)
     */
    @Test
    void pre_image_attack_sha_512_32() {
//        preImageAttack(32, 'C3C0357C')
        def digest = new Sha512nDigest(32)
        assert digest.digest('2872293553') == 'C3C0357C'
    }

    private preImageAttack(int bitOutputSize, String targetHash) {
        def time = Timer.time { println(findPreImage(bitOutputSize, targetHash)) }
        println "${bitOutputSize}bit: ${time}ms"
    }

    private findPreImage(int bitOutputSize, String targetHash) {
        def digest = new Sha512nDigest(bitOutputSize)
        BigInteger i = 0
        for (; digest.digest(i.toString()) != targetHash; i++) {
            if (i % 10_000_000 == 0) {
                println i
            }
        }
        i
    }
}
