package proiect_252.service;

import proiect_252.model.Restaurant;
import proiect_252.model.Address;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RestaurantDatabaseService implements DatabaseService<Restaurant> {
    private static RestaurantDatabaseService instance;
    private final DatabaseConfig dbConfig;
    private final AuditService auditService;
    
    private RestaurantDatabaseService() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.auditService = AuditService.getInstance();
    }
    
    public static RestaurantDatabaseService getInstance() {
        if (instance == null) {
            instance = new RestaurantDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void create(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO restaurants (name, address_id) VALUES (?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, restaurant.getName());
            pstmt.setInt(2, restaurant.getAddress().getId());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    restaurant.setId(generatedKeys.getInt(1));
                }
            }
            auditService.logAction("CREATE_RESTAURANT");
        }
    }
    
    @Override
    public Restaurant read(int id) throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("id"));
                    restaurant.setName(rs.getString("name"));
                    int addressId = rs.getInt("address_id");
                    if (addressId > 0) {
                        AddressDatabaseService addressService = AddressDatabaseService.getInstance();
                        restaurant.setAddress(addressService.read(addressId));
                    }
                    auditService.logAction("READ_RESTAURANT");
                    return restaurant;
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(Restaurant restaurant) throws SQLException {
        String sql = "UPDATE restaurants SET name = ?, address_id = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, restaurant.getName());
            pstmt.setInt(2, restaurant.getAddress().getId());
            pstmt.setInt(3, restaurant.getId());
            pstmt.executeUpdate();
            auditService.logAction("UPDATE_RESTAURANT");
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM restaurants WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_RESTAURANT");
        }
    }
    
    @Override
    public List<Restaurant> readAll() throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT r.*, a.street, a.city, a.state, a.zip_code " +
                    "FROM restaurants r " +
                    "LEFT JOIN addresses a ON r.address_id = a.id";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                
                if (rs.getObject("address_id") != null) {
                    Address address = new Address(
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("zip_code")
                    );
                    address.setId(rs.getInt("address_id"));
                    address.setState(rs.getString("state"));
                    restaurant.setAddress(address);
                }
                
                restaurants.add(restaurant);
            }
            
            auditService.logAction("READ_ALL_RESTAURANTS");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* ignore */ }
            if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignore */ }
        }
        
        return restaurants;
    }
} 