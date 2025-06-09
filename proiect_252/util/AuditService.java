package proiect_252.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String action) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.println(timestamp + " - " + action);
        } catch (IOException e) {
            System.err.println("Error writing to audit log: " + e.getMessage());
        }
    }
} 