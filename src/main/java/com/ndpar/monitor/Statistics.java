package com.ndpar.monitor;

public class Statistics {

    private final String label;

    private long total;
    private long hits;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private long sumOfSquares;

    public Statistics(String label) {
        this.label = label;
    }

    public double getStdDev() {
        double stdDeviation = 0;
        if (hits != 0) {
            double n = hits;
            double nMinus1 = (n <= 1) ? 1 : n - 1; // avoid 0 divides;
            double numerator = sumOfSquares - ((total * total) / n);
            stdDeviation = Math.sqrt(numerator / nMinus1);
        }
        return stdDeviation;
    }

    public void append(Monitor monitor) {
        appendTotal(monitor.total);
        incrementHits();
        updateMin(monitor.total);
        updateMax(monitor.total);
        updateSumOfSquares(total);
    }

    private void appendTotal(long currentDuration) {
        total += currentDuration;
    }

    private void incrementHits() {
        hits++;
    }

    private void updateMin(long currentDuration) {
        if (currentDuration < min)
            min = currentDuration;
    }

    private void updateMax(long currentDuration) {
        if (max < currentDuration)
            max = currentDuration;
    }

    private void updateSumOfSquares(long currentDuration) {
        sumOfSquares += currentDuration * currentDuration;
    }

    public String toString() {
        return String.format("%s[Hits=%d Avg=%,d ns. StdDev=%,.2f ns. Total=%,d ns. Min=%,d ns. Max=%,d ns.]", label, hits,
                getAverage(), getStdDev(), total, min, max);
    }

    public long getAverage() {
        return total / hits;
    }
}
