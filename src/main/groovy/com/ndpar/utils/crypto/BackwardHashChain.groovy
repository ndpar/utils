package com.ndpar.utils.crypto

import java.security.MessageDigest

import static javax.xml.bind.DatatypeConverter.printHexBinary

class BackwardHashChain {

    private BLOCK = 1024
    private md = MessageDigest.getInstance('SHA-256')

    String sha256(String pathname) {
        byte[] bytes = new File(pathname).readBytes()
        byte[] hash = new byte[0]
        int size = bytes.length % BLOCK

        for (int i = bytes.length - size; 0 <= i; i -= BLOCK) {
            byte[] block = new byte[size + hash.length]

            System.arraycopy(bytes, i, block, 0, size)
            System.arraycopy(hash, 0, block, size, hash.length)

            hash = md.digest(block)
            size = BLOCK
        }
        printHexBinary(hash).toLowerCase()
    }
}
