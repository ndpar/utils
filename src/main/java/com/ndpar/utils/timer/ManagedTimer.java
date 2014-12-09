package com.ndpar.utils.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Timer that can be started and stopped multiple times. It can be exposed as an
 * MBean using Spring MBean exporter.
 * <p>
 * Actually, Spring provides very convenient classes to schedule tasks,
 * ScheduledTimerTask and TimerFactoryBean, but they have two restrictions:
 * <ol>
 * <li>You cannot restart the task after you stop it;
 * <li>It's not possible to change timer period on the fly.
 * </ol>
 * If your requirements don't conflict with those restrictions, use Spring
 * timers because they are well tested and more robust.
 * <p>
 * Note: Current implementation is using threads - be careful while deploying
 * this class in application server environment.
 *
 * @author Andrey Paramonov
 */
public class ManagedTimer {

    protected final Log logger = LogFactory.getLog(getClass());

    private long period = -1;
    private final Object task;
    private final String method;

    private boolean runOnStartup;
    private boolean stopOnError;

    private long counter;
    private Thread taskThread;

    // --------------------------------------------------------------
    // Initialization
    // --------------------------------------------------------------

    public ManagedTimer(Object task, String method) {
        this(task, method, false);
    }

    public ManagedTimer(Object task, String method, boolean runOnStartup) {
        this.task = task;
        this.method = method;
        this.runOnStartup = runOnStartup;
        this.stopOnError = true;
    }

    // --------------------------------------------------------------
    // Getters and Setters (MBean attributes)
    // --------------------------------------------------------------

    public long getPeriod() {
        return period;
    }

    /**
     * Sets timer period in milliseconds.
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    public boolean isStopOnError() {
        return stopOnError;
    }

    public void setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public boolean isRunning() {
        return taskThread != null;
    }

    public Object getTask() {
        return task;
    }

    public long getCounter() {
        return counter;
    }

    // --------------------------------------------------------------
    // Managed interface
    // --------------------------------------------------------------

    public void start() throws Exception {
        checkTask();
        if (0 < period) {
            startRepeated();
        } else {
            runTask();
        }
    }

    private void runTask() throws Exception {
        task.getClass().getMethod(method).invoke(task);
    }

    private void startRepeated() {
        if (taskThread == null) {
            taskThread = new Thread(new Runnable() {
                private boolean running = true;

                public void run() {
                    while (running && 0 < period) {
                        try {
                            runTask();
                            ++counter;
                            Thread.sleep(period);
                        } catch (InterruptedException e) {
                            logger.info("Stopping task " + task);
                            running = false;
                        } catch (Exception e) {
                            logger.error("Unexpected Exception caught in task " + task + ": " + e.getMessage(), e);
                            if (stopOnError) {
                                running = false;
                                try {
                                    stop();
                                } catch (Exception exc) {
                                    logger.error(exc.getMessage(), exc);
                                }
                            }
                        }
                    }
                }
            });
            logger.info("Starting task " + task);
            taskThread.start();
        } else {
            logger.warn("Task " + task + " is already started");
        }
    }

    public void stop() throws Exception {
        if (taskThread != null) {
            taskThread.interrupt();
            taskThread = null;
        } else {
            logger.warn("Task " + task + " is already stopped");
        }
    }

    private void checkTask() {
        if (task == null) {
            throw new IllegalStateException("Task is not injected. Check out the Spring configuration file");
        }
    }

    // --------------------------------------------------------------
    // Spring life cycle methods
    // --------------------------------------------------------------

    /**
     * Spring framework invokes this method after ApplicationContext is
     * successfully started:
     *
     * <pre>
     * &lt;bean ... init-method=&quot;init&quot;&gt;
     * </pre>
     */
    public void init() throws Exception {
        if (runOnStartup) {
            start();
        }
    }

    /**
     * Spring framework invokes this method when ApplicationContext is about to
     * be destroyed. Without this method, TimerTask thread would keep running
     * even after the application were undeployed:
     *
     * <pre>
     * &lt;bean ... destroy-method=&quot;destroy&quot;&gt;
     * </pre>
     */
    public void destroy() throws Exception {
        logger.info("Destroying task " + task);
        stop();
    }
}
