package com.ndpar.monitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonitorFactory {

    private Map<String, Statistics> stats;

    public MonitorFactory() {
        stats = Collections.synchronizedMap(new HashMap<String, Statistics>());
    }

    public Monitor start(String label) {
        return new Monitor(label, this);
    }

    public void stop(Monitor monitor) {
        synchronized (stats) {
            Statistics statistics = stats.get(monitor.label);
            if (statistics == null) {
                statistics = new Statistics(monitor.label);
                stats.put(monitor.label, statistics);
            }
            statistics.append(monitor);
        }
    }

    public Statistics getStatistics(String label) {
        return stats.get(label);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString()).append(" Statistics:\n");
        for (Statistics statistics : stats.values()) {
            sb.append(statistics.toString()).append("\n");
        }
        return sb.toString();
    }
}
