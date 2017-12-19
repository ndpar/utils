package com.ndpar.utils.crypto

import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

import static javax.xml.bind.DatatypeConverter.parseHexBinary
import static javax.xml.bind.DatatypeConverter.printHexBinary

abstract class AesGcmCipher {

    static String gmac(String key, String nonce, String data, int tagLen) {
        aesGcm(key, nonce, '', data, tagLen)
    }

    static String aesGcm(String key, String iv, String plaintext, String aad, int tagLen) {
        printHexBinary(aesGcm(
                parseHexBinary(key),
                parseHexBinary(iv),
                parseHexBinary(plaintext),
                parseHexBinary(aad),
                tagLen
        ))
    }

    static byte[] aesGcm(byte[] aesKey, byte[] ivData, byte[] plaintext, byte[] aad, int tagLen) {
        Cipher.getInstance('AES/GCM/NoPadding').with {
            init(ENCRYPT_MODE, new SecretKeySpec(aesKey, 'AES'), new GCMParameterSpec(tagLen, ivData))
            updateAAD(aad)
            doFinal(plaintext)
        }
    }
}
