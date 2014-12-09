package com.ndpar.monitor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PerformanceMonitorInterceptor implements MethodInterceptor {

    private boolean monitorEnabled = true;
    private MonitorFactory monitorFactory;


    public boolean isMonitorEnabled() {
        return monitorEnabled;
    }

    public void setMonitorEnabled(boolean monitorEnabled) {
        this.monitorEnabled = monitorEnabled;
    }

    public void setMonitorFactory(MonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (isMonitorEnabled()) {
            return invokeUnderMonitor(invocation);
        }
        else {
            return invocation.proceed();
        }
    }

    protected Object invokeUnderMonitor(MethodInvocation invocation) throws Throwable {
        String name = createInvocationName(invocation);
        Monitor monitor = monitorFactory.start(name);
        try {
            return invocation.proceed();
        } finally {
            monitor.stop();
        }
    }

    @SuppressWarnings("unchecked")
    protected String createInvocationName(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class clazz = method.getDeclaringClass();

        StringBuffer sb = new StringBuffer(clazz.getName());
        sb.append('.').append(method.getName());
        return sb.toString();
    }
}
