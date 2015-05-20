package com.ndpar.utils.crypto;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.fill;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class BlockCipherModes {

    private static final int BLOCK = 128 / 8; // AES 128 bit
    private static final String ENC = "UTF-8";

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

    private SecretKeySpec keySpec(String key) {
        return new SecretKeySpec(parseHexBinary(key), "AES");
    }

    public String encryptCbc(String encryptionKey, String ivs, String plainText) throws Exception {
        SecretKeySpec key = keySpec(encryptionKey);
        int padLength = padLength(plainText, BLOCK);
        if (padLength == 0) padLength = BLOCK;
        byte[] result = copyOfRange(plainText.getBytes(ENC), 0, plainText.length() + padLength);
        fill(result, result.length - padLength, result.length, (byte) padLength);
        byte[] iv = parseHexBinary(ivs);
        for (int b = 0; BLOCK * b < result.length; b++) {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            iv = cipher.doFinal(copyOfRange(result, BLOCK * b, BLOCK * (b + 1)));
            System.arraycopy(iv, 0, result, BLOCK * b, BLOCK);
        }
        return (ivs + printHexBinary(result)).toLowerCase();
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
        return new String(trimPadding(result), ENC);
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
        return copyOfRange(text, 0, text.length - n);
    }

    private int padLength(String string, int multiples) {
        return padLength(string.length(), multiples);
    }

    private int padLength(byte[] bytes, int multiples) {
        return padLength(bytes.length, multiples);
    }

    private int padLength(int length, int multiples) {
        while (length > multiples) length -= multiples;
        return (multiples - length) % multiples;
    }

    public byte[] pad(byte[] bytes) {
        int padLength = padLength(bytes, BLOCK);
        byte[] result = new byte[bytes.length + padLength];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        for (int i = 0; i < padLength; i++) result[bytes.length + i] = (byte) padLength;
        return result;
    }

    private byte[] ctr(String encryptionKey, byte[] ivs, byte[] plainText) throws Exception {
        int padLength = padLength(plainText, BLOCK);
        byte[] result = copyOfRange(plainText, 0, plainText.length + padLength);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec(encryptionKey), new IvParameterSpec(new byte[BLOCK]));
        BigInteger iv = new BigInteger(ivs);
        for (int b = 0; BLOCK * b < result.length; b++) {
            byte[] biv = add(iv, b).toByteArray();
            byte[] m = xor(cipher.doFinal(biv), copyOfRange(result, BLOCK * b, BLOCK * (b + 1)));
            System.arraycopy(m, 0, result, BLOCK * b, BLOCK);

        }
        return copyOfRange(result, 0, result.length - padLength);
    }

    private BigInteger add(BigInteger bi, int i) {
        return bi.add(BigInteger.valueOf(i));
    }

    public byte[] xor(byte[] a1, byte[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) a1[i] ^= a2[i];
        return a1;
    }

    public String encryptCtr(String encryptionKey, String ivs, String plainText) throws Exception {
        byte[] result = ctr(encryptionKey, parseHexBinary(ivs), plainText.getBytes(ENC));
        return (ivs + printHexBinary(result)).toLowerCase();
    }

    public String decryptCtr(String encryptionKey, String cipherText) throws Exception {
        byte[] cb = parseHexBinary(cipherText);
        byte[] result = ctr(encryptionKey, copyOfRange(cb, 0, BLOCK), copyOfRange(cb, BLOCK, cb.length));
        return new String(result, ENC);
    }
}
