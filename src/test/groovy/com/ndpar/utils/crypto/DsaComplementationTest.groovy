package com.ndpar.utils.crypto

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

import static javax.xml.bind.DatatypeConverter.parseHexBinary
import static javax.xml.bind.DatatypeConverter.printHexBinary

/**
 * Usage: groovy DsaComplementationTest <64-bit key in hex> <64-bit text block in hex>
 * E.g.: groovy DsaComplementationTest 0102030405060708 1020304050607080
 */
class DsaComplementationTest {

    static iv = new IvParameterSpec([0, 0, 0, 0, 0, 0, 0, 0] as byte[])

    static void main(String[] args) {
        def keyHex = args[0]
        def textHex = args[1]

        def cipherHex = encrypt(keyHex, textHex)
        println "E($keyHex, $textHex) = $cipherHex"

        def cKeyHex = complement(keyHex)
        def cTextHex = complement(textHex)

        def cCipherHex = encrypt(cKeyHex, cTextHex)
        println "E($cKeyHex, $cTextHex) = $cCipherHex"

        assert cCipherHex == complement(cipherHex)
    }

    static encrypt(String keyHex, String textHex) {
        def keyBytes = parseHexBinary(keyHex)
        def textBytes = parseHexBinary(textHex)

        def key = SecretKeyFactory.getInstance('DES').generateSecret(new DESKeySpec(keyBytes))

        def cipher = Cipher.getInstance('DES/CBC/NoPadding')
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)

        def cipherBytes = cipher.doFinal(textBytes)
        printHexBinary(cipherBytes)
    }

    static complement(String hex) {
        def bytes = parseHexBinary(hex)
        def result = new byte[bytes.length]
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes[i] ^ 0xFF
        }
        printHexBinary(result)
    }
}
