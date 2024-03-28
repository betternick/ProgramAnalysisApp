package org.profile;

// Logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CPU usage
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class Log {
    // Asynchronous logger or a queue for batching writes to the file
    private static final Logger logger = LogManager.getLogger(Log.class);

    // Where is place of the log
    public static String getLogPath() {
        return "logs/target.log";
    }

    public static void logNormalInfo(String identifier) {
        // Log the current time
        long currentTime = System.nanoTime();
        logger.info(identifier + " - Time: " + currentTime);

        System.out.println("Logging path: " + getLogPath());

        // Log the memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        logger.fatal(identifier + " - Memory: " + usedMemory);

        // Log the CPU usage
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getProcessCpuLoad();
        logger.fatal(identifier + " - CPU: " + cpuLoad);
    }
}
