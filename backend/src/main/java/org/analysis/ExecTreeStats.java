package org.analysis;

public class ExecTreeStats {
    public int executionTimes;
    public double averageExecutionTime;
    public double averageMemoryUsage;
    public double averageCpuUsage;

    public ExecTreeStats(int executionTimes, double averageExecutionTime, double averageMemoryUsage,
            double averageCpuUsage) {
        this.executionTimes = executionTimes;
        this.averageExecutionTime = averageExecutionTime;
        this.averageMemoryUsage = averageMemoryUsage;
        this.averageCpuUsage = averageCpuUsage;
    }
}
