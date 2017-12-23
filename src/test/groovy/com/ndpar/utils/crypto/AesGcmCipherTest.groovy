package com.ndpar.utils.crypto

import org.junit.Test

class AesGcmCipherTest {


    @Test
    void test_gmac() {
        assert '34B025A57D99315120912DEFBFE329C3' == AesGcmCipher.gmac(
                '8000000000000000000000000000000000000000000000000000000000000001',
                '000000000000000000000001',
                '4d4143732061726520766572792075736566756c20696e2063727970746f67726170687921'
        )
    }

    // ------------------------------------------------------------------------
    // http://csrc.nist.gov/groups/STM/cavp/documents/mac/gcmtestvectors.zip
    // ------------------------------------------------------------------------

    @Test
    void nist_256_96_0_0_128() {
        assert 'BDC1AC884D332457A1D2664F168C76F0' == AesGcmCipher.aesGcm(
                'b52c505a37d78eda5dd34f20c22540ea1b58963cf8e5bf8ffa85f9f2492505b4',
                '516c33929df5a3284ff463d7',
                '',
                '',
                128
        )
    }

    @Test
    void nist_256_96_0_128_128() {
        assert '3E5D486AA2E30B22E040B85723A06E76' == AesGcmCipher.aesGcm(
                '78dc4e0aaf52d935c3c01eea57428f00ca1fd475f5da86a49c8dd73d68c8e223',
                'd79cf22d504cc793c3fb6c8a',
                '',
                'b96baa8c1c75a671bfb2d08d06be5f36',
                128
        )
    }

    @Test
    void nist_256_96_128_0_128() {
        def ciphertext = 'FA4362189661D163FCD6A56D8BF0405A'
        def tag = 'D636AC1BBEDD5CC3EE727DC2AB4A9489'
        assert ciphertext + tag == AesGcmCipher.aesGcm(
                '31bdadd96698c204aa9ce1448ea94ae1fb4a9a0b3c9d773b51bb1822666b8f22',
                '0d18e06c7c725ac9e362e1ce',
                '2db5168e932556f8089a0622981d017d',
                '',
                128
        )
    }

    @Test
    void nist_256_96_128_128_128() {
        def ciphertext = '8995AE2E6DF3DBF96FAC7B7137BAE67F'
        def tag = 'ECA5AA77D51D4A0A14D9C51E1DA474AB'
        assert ciphertext + tag == AesGcmCipher.aesGcm(
                '92e11dcdaa866f5ce790fd24501f92509aacf4cb8b1339d50c9c1240935dd08b',
                'ac93a1a6145299bde902f21a',
                '2d71bcfa914e4ac045b2aa60955fad24',
                '1e0889016f67601c8ebea4943bc23ad6',
                128
        )
    }
}
