package org.profile;

// Logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CPU usage
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import java.nio.file.*;
import java.io.IOException;

public class Log {
    // Asynchronous logger or a queue for batching writes to the file
    private static final Logger logger = LogManager.getLogger(Log.class);

    private Log() {
        // Delete the log file
        try {
            Files.deleteIfExists(Paths.get(getLogPath()));
        } catch (IOException e) {
            System.out.println("Can't deltet log file");
        }
    }

    // Where is place of the log
    public static String getLogPath() {
        return "logs/target.log";
    }

    public static void enter(String identifier) {
        long currentTime = System.nanoTime();
        logger.info("Enter:" + identifier + ":" + currentTime);
    }

    public static void exit(String identifier) {
        long currentTime = System.nanoTime();
        logger.info("Exit:" + identifier + ":" + currentTime);
    }

    public static void logNormalInfo(String identifier) {
        // Log the current time
        long currentTime = System.nanoTime();
        // Log the memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        // Log the CPU usage
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getProcessCpuLoad();

        logger.info(identifier + ":Time:" + currentTime + ":Memory:" + usedMemory + ":CPU:" + cpuLoad);
    }
}
