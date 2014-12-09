package com.ndpar.monitor;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MonitorIntegrationTest {

    private BeanFactory beanFactory;

    @Before
    public void setUp() {
        beanFactory = new ClassPathXmlApplicationContext("classpath:ac-monitor.xml");
    }

    @Test
    public void printStatistics() {
        MonitorTestSerivce bean = (MonitorTestSerivce) beanFactory.getBean("testSerivce");
        bean.slowTest();

        for (int i = 0; i < 10; i++)
            bean.fastTest();

        MonitorFactory monitorFactory = (MonitorFactory) beanFactory.getBean("nanoMonitorFactory");
        assertTrue(monitorFactory.toString().contains(MonitorTestSerivce.class.getName()));

        System.out.println(monitorFactory);
    }
}
