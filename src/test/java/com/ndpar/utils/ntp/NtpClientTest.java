package com.ndpar.utils.ntp;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NtpClientTest {

    private NtpClient client;

    @Before
    public void setUp() {
        client = new NtpClient(19003, 20000);
    }

    @After
    public void tearDown() {
        client.destroy();
    }

    @Test
    public void localhostTimeOffsetMustBeCloseToZero() {
        client.setServerHost("localhost");
        long timeOffset = client.calculateTimeOffset();
        System.out.println("Time offset: " + timeOffset);
        assertTrue(0 < 60 - Math.abs(timeOffset));
    }
}
