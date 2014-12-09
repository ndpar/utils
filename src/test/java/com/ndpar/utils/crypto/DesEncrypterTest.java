package com.ndpar.utils.crypto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DesEncrypterTest {

    private DesEncrypter encrypter;

    @Before
    public void setUp() {
        encrypter = new DesEncrypter("super secret password");
    }

    @Test
    public void testEncryption() {
        assertEquals("lciZiT/aMBF8F8U5jhaUXg==", encrypter.encrypt("Lorem ipsum"));
    }

    @Test
    public void testDecryption() {
        assertEquals("Lorem ipsum", encrypter.decrypt("lciZiT/aMBF8F8U5jhaUXg=="));
    }
}
