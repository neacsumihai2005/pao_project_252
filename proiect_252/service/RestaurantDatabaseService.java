package proiect_252.service;

import proiect_252.model.Restaurant;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    // Note: You'll need to load the address separately
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
        String sql = "SELECT * FROM restaurants";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                // Note: You'll need to load the address separately
                restaurants.add(restaurant);
            }
            auditService.logAction("READ_ALL_RESTAURANTS");
        }
        return restaurants;
    }
} 