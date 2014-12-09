package com.ndpar.utils.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimerTask {

    protected final Log logger = LogFactory.getLog(getClass());

    public void justMethod() {
        logger.info("Timer task invoked");
    }
}
