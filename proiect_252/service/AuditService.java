package proiect_252.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit_log.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private AuditService() {
        // Create file with header if it doesn't exist
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            if (writer.checkError()) {
                writer.println("action_name,timestamp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }
    
    public void logAction(String actionName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.println(actionName + "," + timestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 