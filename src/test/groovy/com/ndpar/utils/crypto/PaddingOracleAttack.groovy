package com.ndpar.utils.crypto

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static javax.xml.bind.DatatypeConverter.parseHexBinary
import static javax.xml.bind.DatatypeConverter.printHexBinary

class PaddingOracleAttack {

    private static final BLOCK = 16

    def http = new HTTPBuilder('http://crypto-class.appspot.com/po')


    String attack(cipherHex) {
        def result = []
        byte[] o = parseHexBinary(cipherHex)
        for (byte[] orig = o; BLOCK < orig.length; orig = orig[0..-1 - BLOCK]) {
            byte[] plainBytes = new byte[16]
            for (int i = -17; -32 <= i; i--) {
                boolean tryAgain = true
                byte[] cb = Arrays.copyOf(orig, orig.length)
                for (int k = i + 17; k < 0; k++) {
                    cb[k - 16] = (byte) (cb[k - 16] ^ plainBytes[k] ^ (-16 - i))
                }
                for (byte z = 0; z <= Byte.MAX_VALUE && tryAgain; z++) {
                    cb[i] = (byte) (orig[i] ^ z ^ (-16 - i))
                    def status = test(cb)
                    if (status == 404) {
                        println(z + ' ' + printHexBinary(cb))
                        plainBytes[16 + i] = z
                        tryAgain = false
                    } else if (status == 200 && z == plainBytes[-1]) {
                        println z
                        plainBytes[16 + i] = z
                        tryAgain = false
                    }
                }
            }
            result << plainBytes
            println plainBytes
        }
        byte[] res = result.reverse().flatten() as byte[]
        res = res[0..-1 - res[-1]]
        println res
        new String(res, 'UTF-8')
    }

    private test(byte[] cipherBytes) {
        http.request(Method.GET, ContentType.TEXT) {
            uri.query = [er: printHexBinary(cipherBytes)]
            def result
            response.success = { resp, reader ->
                result = resp.statusLine.statusCode
            }
            response.failure = { resp, reader ->
                result = resp.statusLine.statusCode
            }
            result
        }
    }

    public static void main(String[] args) {
        def attack = new PaddingOracleAttack()
        println(attack.attack('f20bdba6ff29eed7b046d1df9fb7000058b1ffb4210a580f748b4ac714c001bd4a61044426fb515dad3f21f18aa577c0bdf302936266926ff37dbf7035d5eeb4'))
    }
}
