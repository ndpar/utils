package com.ndpar.utils.crypto

import org.junit.Test

class Sha512nDigestBirthdayAttackTest {

    /**
     * Trials: expected 19, actual 10
     * Time: 67.6ms
     * Collision: 10; 6 => 3C
     * $ echo '3130' | xxd -r -p | shasum -ba 512 | head -c 2
     * $ echo '36'   | xxd -r -p | shasum -ba 512 | head -c 2
     */
    @Test
    void birthday_attack_sha_512_8() {
        birthdayAttack(8)
        def digest = new Sha512nDigest(8)
        assert digest.digest('10') == digest.digest('6')
    }

    /**
     * Trials: expected 302, actual 372
     * Time: 61.8ms
     * Collision: 372; 105 => 03D2
     * $ echo '313035' | xxd -r -p | shasum -ba 512 | head -c 4
     * $ echo '333732' | xxd -r -p | shasum -ba 512 | head -c 4
     */
    @Test
    void birthday_attack_sha_512_16() {
//        birthdayAttack(16)
        def digest = new Sha512nDigest(16)
        assert digest.digest('372') == digest.digest('105')
    }

    /**
     * Trials: expected 4823, actual 6318
     * Time: 157ms
     * Collision: 6318; 898 => 121165
     * $ echo '36333138' | xxd -r -p | shasum -ba 512 | head -c 6
     * $ echo '383938'   | xxd -r -p | shasum -ba 512 | head -c 6
     */
    @Test
    void birthday_attack_sha_512_24() {
//        birthdayAttack(24)
        def digest = new Sha512nDigest(24)
        assert digest.digest('6318') == digest.digest('898')
    }

    /**
     * Trials: expected 77163, actual 93656
     * Time: 550.8ms
     * Collision: 93656; 14183 => 2D83D66F
     * $ echo '3933363536' | xxd -r -p | shasum -ba 512 | head -c 8
     * $ echo '3134313833' | xxd -r -p | shasum -ba 512 | head -c 8
     */
    @Test
    void birthday_attack_sha_512_32() {
//        birthdayAttack(32)
        def digest = new Sha512nDigest(32)
        assert digest.digest('93656') == digest.digest('14183')
    }

    /**
     * Trials: expected 1234604, actual 2443641
     * Time: 14736ms
     * Collision: 2443641; 411210 => 5232228D80
     * $ echo '32343433363431' | xxd -r -p | shasum -ba 512 | head -c 10
     * $ echo '343131323130'   | xxd -r -p | shasum -ba 512 | head -c 10
     */
    @Test
    void birthday_attack_sha_512_40() {
//        birthdayAttack(40) // slow
        def digest = new Sha512nDigest(40)
        assert digest.digest('2443641') == digest.digest('411210')
    }

    /**
     * Trials: expected 19753663, actual 12965823
     * Time: 232775.2ms
     * Collision: 12965823; 7858609 => 1A0922E4701D
     * $ echo '3132393635383233' | xxd -r -p | shasum -ba 512 | head -c 12
     * $ echo '37383538363039'   | xxd -r -p | shasum -ba 512 | head -c 12
     */
    @Test
    void birthday_attack_sha_512_48() {
//        birthdayAttack(48) // slow
        def digest = new Sha512nDigest(48)
        assert digest.digest('12965823') == digest.digest('7858609')
    }

    private birthdayAttack(int bitOutputSize) {
        def time = Timer.time { println(findCollision(bitOutputSize)) }
        println "${bitOutputSize}bit: ${time}ms"
    }

    private findCollision(int bitOutputSize) {
        def digest = new Sha512nDigest(bitOutputSize)
        def cache = [:]
        def cacheSize = 2.power(bitOutputSize)
        for (def i = 0; i < cacheSize; i++) {
            def hash = digest.digest(i.toString())
            if (cache[hash]) {
                return "${i.toString()};${cache[hash]}=${hash}"
            } else {
                cache[hash] = i.toString()
            }
        }
    }

    @Test
    void expected_birthday_collision_sha_512_8() {
        assert 19 == expectedBirthdayCollision(8)
    }

    @Test
    void expected_birthday_collision_sha_512_16() {
        assert 302 == expectedBirthdayCollision(16)
    }

    @Test
    void expected_birthday_collision_sha_512_24() {
        assert 4823 == expectedBirthdayCollision(24)
    }

    @Test
    void expected_birthday_collision_sha_512_32() {
        assert 77163 == expectedBirthdayCollision(32)
    }

    @Test
    void expected_birthday_collision_sha_512_40() {
        assert 1234604 == expectedBirthdayCollision(40)
    }

    @Test
    void expected_birthday_collision_sha_512_48() {
        assert 19753663 == expectedBirthdayCollision(48)
    }

    private expectedBirthdayCollision(int bitOutputSize) {
        def a = 2 * Math.log(2)
        Math.round(0.5 + Math.sqrt(0.25 + a * 2.power(bitOutputSize)))
    }
}
