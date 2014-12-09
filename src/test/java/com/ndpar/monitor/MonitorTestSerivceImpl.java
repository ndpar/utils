package com.ndpar.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MonitorTestSerivceImpl implements MonitorTestSerivce {

    protected final Log logger = LogFactory.getLog(getClass());

    public void fastTest() {
    }

    public void slowTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
