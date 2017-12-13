package com.ndpar.utils.crypto

import org.codehaus.groovy.runtime.DefaultGroovyMethods as M

import javax.xml.bind.DatatypeConverter as DC

abstract class HexStrings {

    static String toAscii(String string) {
        new String(DC.parseHexBinary(hexDigits.call(string)))
    }

    private static hexDigits = {
        String str -> str.replaceAll(/[ .]/, '')
    }

    static String xor(String... args) {
        hexTransform(M.&xor, args)
    }

    private static String hexTransform(Closure transform, String... args) {
        DC.printHexBinary(args
                .collect(hexDigits)
                .collect(DC.&parseHexBinary)
                .inject(operation(transform)))
    }

    private static operation = { Closure f ->
        { byte[] a, byte[] b -> operate a, b, f }
    }

    private static byte[] operate(byte[] a, byte[] b, Closure f) {
        [a, b].transpose().collect f
    }
}
