package com.ndpar.utils.crypto

import org.junit.Before
import org.junit.Test

import static javax.xml.bind.DatatypeConverter.parseHexBinary
import static javax.xml.bind.DatatypeConverter.printHexBinary

class BlockCipherModesTest {

    private BlockCipherModes cipher

    @Before
    void before() {
        cipher = new BlockCipherModes()
    }

    @Test
    void encryptCbc_padding_8() {
        def text = cipher.encryptCbc('140b41b22a29beb4061bda66b6747e14', '4ca00ff4c898d61e1edbf1800618fb28', 'Basic CBC mode encryption needs padding.')
        assert text == '4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81'
    }

    @Test
    void decryptCbc_padding_8() {
        def text = cipher.decryptCbc('140b41b22a29beb4061bda66b6747e14', '4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81')
        assert text == 'Basic CBC mode encryption needs padding.'
    }

    @Test
    void encryptCbc_padding_16() {
        def text = cipher.encryptCbc('140b41b22a29beb4061bda66b6747e14', '5b68629feb8606f9a6667670b75b38a5', 'Our implementation uses rand. IV')
        assert text == '5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253'
    }

    @Test
    void decryptCbc_padding_16() {
        def text = cipher.decryptCbc('140b41b22a29beb4061bda66b6747e14', '5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253')
        assert text == 'Our implementation uses rand. IV'
    }

    @Test
    void encryptCtr_1() {
        def text = cipher.encryptCtr('36f18357be4dbd77f050515c73fcf9f2', '69dda8455c7dd4254bf353b773304eec', 'CTR mode lets you build a stream cipher from a block cipher.')
        assert text == '69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329'
    }

    @Test
    void decryptCtr_1() {
        def text = cipher.decryptCtr('36f18357be4dbd77f050515c73fcf9f2', '69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329')
        assert text == 'CTR mode lets you build a stream cipher from a block cipher.'
    }

    @Test
    void encryptCtr_2() {
        def text = cipher.encryptCtr('36f18357be4dbd77f050515c73fcf9f2', '770b80259ec33beb2561358a9f2dc617', 'Always avoid the two time pad!')
        assert text == '770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451'
    }

    @Test
    void decryptCtr_2() {
        def text = cipher.decryptCtr('36f18357be4dbd77f050515c73fcf9f2', '770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451')
        assert text == 'Always avoid the two time pad!'
    }

    @Test
    void forgeCbcCipher() {
        def cipherHex = '20814804c1767293b99f1d9cab3bc3e7ac1e37bfb15599e5f40eef805488281d'
        def orig = 'Pay Bob 100$'
        def forged = 'Pay Bob 500$'

        byte[] iv = parseHexBinary(cipherHex)[0..15]
        byte[] origBytes = cipher.pad(orig.bytes)
        byte[] forgedBytes = cipher.pad(forged.bytes)

        byte[] newIv = cipher.xor(iv, cipher.xor(origBytes, forgedBytes))
        def newCipherHex = (printHexBinary(newIv) + cipherHex[32..-1]).toLowerCase()

        assert newCipherHex == '20814804c1767293bd9f1d9cab3bc3e7ac1e37bfb15599e5f40eef805488281d'
    }
}
