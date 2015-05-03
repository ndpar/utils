package com.ndpar.utils.crypto;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class BlockCipherModes {

    private static final int BLOCK = 128 / 8; // AES 128 bit

    private Cipher cipher;

    public BlockCipherModes() {
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding"); // we only need AES
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public String decryptCbc(String encryptionKey, String cipherText) throws Exception {
        byte[][] c = split16(dfs(cipherText));
        SecretKeySpec key = new SecretKeySpec(dfs(encryptionKey), "AES");
        byte[] result = new byte[BLOCK * (c.length - 1)]; // c[0] is IV
        for (int i = c.length - 1; 0 < i; i--) {
            byte[] iv = c[i - 1];
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] m = cipher.doFinal(c[i]);
            System.arraycopy(m, 0, result, BLOCK * (i - 1), BLOCK);
        }
        return new String(trimPadding(result), "UTF-8");
    }

    private byte[] dfs(String hexString) {
        String[] hexes = hexString.split("(?<=\\G..)");
        byte[] result = new byte[hexes.length];
        for (int i = 0; i < result.length; i++) result[i] = (byte) Integer.parseInt(hexes[i], BLOCK);
        return result;
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

    public String decryptCtr(String encryptionKey, String cipherText) throws Exception {
        byte[] cipher = dfs(cipherText);
        int padLength = padLength(cipher.length, BLOCK);
        byte[][] c = split16(pad(cipher, padLength));
        BigInteger iv = new BigInteger(c[0]);
        SecretKeySpec key = new SecretKeySpec(dfs(encryptionKey), "AES");
        this.cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[BLOCK]));
        byte[] result = new byte[BLOCK * (c.length - 1)];
        for (int i = 1; i < c.length; i++) { // skip iv
            byte[] biv = add(iv, i - 1).toByteArray();
            byte[] m = xor(this.cipher.doFinal(biv), c[i]);
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
