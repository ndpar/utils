package com.ndpar.utils.crypto

import java.security.MessageDigest

import static javax.xml.bind.DatatypeConverter.parseHexBinary
import static javax.xml.bind.DatatypeConverter.printHexBinary

class Sha512nDigest {

    final byteOutputSize
    final md = MessageDigest.getInstance('SHA-512')

    Sha512nDigest(int bitOutputSize) {
        this.byteOutputSize = bitOutputSize / 8
    }

    String digest(String string) {
        printHexBinary(digest(string.bytes))
    }

    String digestHex(String hexString) {
        printHexBinary(digest(parseHexBinary(hexString)))
    }

    byte[] digest(byte[] bytes) {
        md.digest(bytes)[0..<byteOutputSize]
    }
}
