package com.ndpar.utils.crypto;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class BlockCipherModes {

    private static final int BLOCK = 128 / 8; // AES 128 bit

    private Cipher cipher;

    public BlockCipherModes() {
        try {
            // We only need algorithm (AES).
            // We implement mode and padding ourselves.
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public String encryptCbc(String encryptionKey, String ivs, String plainText) throws Exception {
        SecretKeySpec key = keySpec(encryptionKey);
        int padLength = padLength(plainText.length(), BLOCK);
        if (padLength == 0) padLength = BLOCK;
        byte[] result = Arrays.copyOfRange(plainText.getBytes("UTF-8"), 0, plainText.length() + padLength);
        Arrays.fill(result, result.length - padLength, result.length, (byte) padLength);
        byte[] iv = parseHexBinary(ivs);
        for (int b = 0; BLOCK * b < result.length; b++) {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            iv = cipher.doFinal(Arrays.copyOfRange(result, BLOCK * b, BLOCK * (b + 1)));
            System.arraycopy(iv, 0, result, BLOCK * b, BLOCK);
        }
        return (ivs + printHexBinary(result)).toLowerCase();
    }

    private SecretKeySpec keySpec(String key) {
        return new SecretKeySpec(parseHexBinary(key), "AES");
    }

    public String decryptCbc(String encryptionKey, String cipherText) throws Exception {
        SecretKeySpec key = keySpec(encryptionKey);
        byte[][] c = split16(parseHexBinary(cipherText));
        byte[] result = new byte[BLOCK * (c.length - 1)]; // c[0] is IV
        for (int i = c.length - 1; 0 < i; i--) {
            byte[] iv = c[i - 1];
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] m = cipher.doFinal(c[i]);
            System.arraycopy(m, 0, result, BLOCK * (i - 1), BLOCK);
        }
        return new String(trimPadding(result), "UTF-8");
    }

    private byte[][] split16(byte[] bytes) {
        byte[][] result = new byte[bytes.length / BLOCK][BLOCK];
        for (int i = 0; i < bytes.length / BLOCK; i++) {
            System.arraycopy(bytes, BLOCK * i, result[i], 0, BLOCK);
        }
        return result;
    }

    private byte[] trimPadding(byte[] text) {
        int n = text[text.length - 1]; // last byte contains the length of pad
        return Arrays.copyOfRange(text, 0, text.length - n);
    }

    public String encryptCtr(String encryptionKey, String ivs, String plainText) throws Exception {
        int padLength = padLength(plainText.length(), BLOCK);
        byte[] result = Arrays.copyOfRange(plainText.getBytes("UTF-8"), 0, plainText.length() + padLength);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec(encryptionKey), new IvParameterSpec(new byte[BLOCK]));
        BigInteger iv = new BigInteger(parseHexBinary(ivs));
        for (int b = 0; BLOCK * b < result.length; b++) {
            byte[] biv = add(iv, b).toByteArray();
            byte[] m = xor(cipher.doFinal(biv), Arrays.copyOfRange(result, BLOCK * b, BLOCK * (b + 1)));
            System.arraycopy(m, 0, result, BLOCK * b, BLOCK);

        }
        return (ivs + printHexBinary(Arrays.copyOfRange(result, 0, result.length - padLength))).toLowerCase();
    }

    public String decryptCtr(String encryptionKey, String cipherText) throws Exception {
        byte[] cb = parseHexBinary(cipherText);
        int padLength = padLength(cb.length, BLOCK);
        byte[][] c = split16(pad(cb, padLength));
        BigInteger iv = new BigInteger(c[0]);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec(encryptionKey), new IvParameterSpec(new byte[BLOCK]));
        byte[] result = new byte[BLOCK * (c.length - 1)];
        for (int i = 1; i < c.length; i++) { // skip iv
            byte[] biv = add(iv, i - 1).toByteArray();
            byte[] m = xor(cipher.doFinal(biv), c[i]);
            System.arraycopy(m, 0, result, BLOCK * (i - 1), BLOCK);
        }
        return new String(Arrays.copyOfRange(result, 0, result.length - padLength), "UTF-8");
    }

    private int padLength(int length, int multiples) {
        while (length > multiples) length -= multiples;
        return (multiples - length) % multiples;
    }

    private byte[] pad(byte[] text, int padLength) {
        return Arrays.copyOfRange(text, 0, text.length + padLength);
    }

    private BigInteger add(BigInteger bi, int i) {
        return bi.add(BigInteger.valueOf(i));
    }

    private byte[] xor(byte[] a1, byte[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) a1[i] ^= a2[i];
        return a1;
    }
}
