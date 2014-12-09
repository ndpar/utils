package com.ndpar.monitor;

public class Monitor {

    protected final String label;
    private final MonitorFactory factory;
    private final long start;

    protected long total;

    public Monitor(String label, MonitorFactory factory) {
        this.label = label;
        this.factory = factory;
        this.start = System.nanoTime();
    }

    public void stop() {
        total = System.nanoTime() - start;
        factory.stop(this);
    }
}
