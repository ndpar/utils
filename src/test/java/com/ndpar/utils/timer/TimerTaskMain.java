package com.ndpar.utils.timer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * To launch this app in Eclipse, run /src/test/scripts/TimerTaskMain.launch
 *
 * To change period, star and stop timer, use jconsole
 */
public class TimerTaskMain {

    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("ac-timers.xml");
    }
}
