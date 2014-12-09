package com.ndpar.monitor;

import org.junit.Before;
import org.junit.Test;

public class MonitorTest {

    private MonitorFactory factory;

    @Before
    public void setUp() {
        factory = new MonitorFactory();
    }

    @Test
    public void test() throws Exception {
        Monitor mon=null;
        for (int i=1; i<=10; i++) {
            mon = factory.start("myFirstMonitor");
            Thread.sleep(100+i);
            mon.stop();

        }
        System.out.println(factory.getStatistics("myFirstMonitor"));
    }
}
