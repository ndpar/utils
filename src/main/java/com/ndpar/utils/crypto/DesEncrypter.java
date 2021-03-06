package com.ndpar.utils.crypto;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * See http://www.exampledepot.com/egs/javax.crypto/PassKey.html
 * See http://java.sun.com/j2se/1.5.0/docs/guide/security/jce/JCERefGuide.html
 * See http://www.jasypt.org
 */
public class DesEncrypter {

    private Cipher ecipher;
    private Cipher dcipher;

    byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    int iterationCount = 19;

    public DesEncrypter(String passPhrase) {
        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            return new String(Base64.getEncoder().encode(enc));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String str) {
        try {
            byte[] dec = Base64.getDecoder().decode(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
