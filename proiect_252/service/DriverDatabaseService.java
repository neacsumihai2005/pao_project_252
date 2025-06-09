package proiect_252.service;

import proiect_252.model.Driver;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDatabaseService implements DatabaseService<Driver> {
    private static DriverDatabaseService instance;
    private final DatabaseConfig dbConfig;
    private final AuditService auditService;
    
    private DriverDatabaseService() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.auditService = AuditService.getInstance();
    }
    
    public static DriverDatabaseService getInstance() {
        if (instance == null) {
            instance = new DriverDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void create(Driver driver) throws SQLException {
        String sql = "INSERT INTO drivers (name, vehicle_type, license_number) VALUES (?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getVehicleType());
            pstmt.setString(3, driver.getLicenseNumber());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    driver.setId(generatedKeys.getInt(1));
                }
            }
            auditService.logAction("CREATE_DRIVER");
        }
    }
    
    @Override
    public Driver read(int id) throws SQLException {
        String sql = "SELECT * FROM drivers WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Driver driver = new Driver(
                        rs.getString("name"),
                        rs.getString("vehicle_type")
                    );
                    driver.setId(rs.getInt("id"));
                    driver.setLicenseNumber(rs.getString("license_number"));
                    auditService.logAction("READ_DRIVER");
                    return driver;
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(Driver driver) throws SQLException {
        String sql = "UPDATE drivers SET name = ?, vehicle_type = ?, license_number = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getVehicleType());
            pstmt.setString(3, driver.getLicenseNumber());
            pstmt.setInt(4, driver.getId());
            pstmt.executeUpdate();
            auditService.logAction("UPDATE_DRIVER");
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM drivers WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_DRIVER");
        }
    }
    
    @Override
    public List<Driver> readAll() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Driver driver = new Driver(
                    rs.getString("name"),
                    rs.getString("vehicle_type")
                );
                driver.setId(rs.getInt("id"));
                driver.setLicenseNumber(rs.getString("license_number"));
                drivers.add(driver);
            }
            auditService.logAction("READ_ALL_DRIVERS");
        }
        return drivers;
    }
} 