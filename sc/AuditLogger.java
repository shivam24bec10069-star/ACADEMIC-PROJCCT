package com.logiflow.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread-safe audit logger for recording operational actions and telemetry to disk.
 */
public class AuditLogger {
    private static final String DEFAULT_LOG_PATH = "data/audit.log";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static volatile AuditLogger instance;
    private final File logFile;

    private AuditLogger(String filePath) {
        this.logFile = new File(filePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    public static AuditLogger getInstance() {
        if (instance == null) {
            synchronized (AuditLogger.class) {
                if (instance == null) {
                    instance = new AuditLogger(DEFAULT_LOG_PATH);
                }
            }
        }
        return instance;
    }

    public synchronized void log(String level, String component, String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String formattedLine = String.format("[%s] [%-5s] [%-15s] %s", timestamp, level, component, message);

        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(formattedLine);
        } catch (IOException e) {
            System.err.println("Warning: Unable to write to audit log: " + e.getMessage());
        }
    }

    public void info(String component, String message) {
        log("INFO", component, message);
    }

    public void warn(String component, String message) {
        log("WARN", component, message);
    }

    public void error(String component, String message) {
        log("ERROR", component, message);
    }

    public void dispatch(String component, String message) {
        log("DISP", component, message);
    }
}
