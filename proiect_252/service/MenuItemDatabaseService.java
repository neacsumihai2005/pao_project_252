package proiect_252.service;

import proiect_252.model.MenuItem;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDatabaseService implements DatabaseService<MenuItem> {
    private static MenuItemDatabaseService instance;
    private final DatabaseConfig dbConfig;
    private final AuditService auditService;
    
    private MenuItemDatabaseService() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.auditService = AuditService.getInstance();
    }
    
    public static MenuItemDatabaseService getInstance() {
        if (instance == null) {
            instance = new MenuItemDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void create(MenuItem item) throws SQLException {
        String sql = "INSERT INTO menu_items (restaurant_id, name, description, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, item.getRestaurantId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setDouble(4, item.getPrice());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
            auditService.logAction("CREATE_MENU_ITEM");
        }
    }
    
    @Override
    public MenuItem read(int id) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getString("name"),
                        rs.getDouble("price")
                    );
                    item.setId(rs.getInt("id"));
                    item.setRestaurantId(rs.getInt("restaurant_id"));
                    item.setDescription(rs.getString("description"));
                    auditService.logAction("READ_MENU_ITEM");
                    return item;
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(MenuItem item) throws SQLException {
        String sql = "UPDATE menu_items SET name = ?, description = ?, price = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, item.getId());
            pstmt.executeUpdate();
            auditService.logAction("UPDATE_MENU_ITEM");
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_MENU_ITEM");
        }
    }
    
    @Override
    public List<MenuItem> readAll() throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getString("name"),
                    rs.getDouble("price")
                );
                item.setId(rs.getInt("id"));
                item.setRestaurantId(rs.getInt("restaurant_id"));
                item.setDescription(rs.getString("description"));
                items.add(item);
            }
            auditService.logAction("READ_ALL_MENU_ITEMS");
        }
        return items;
    }
    
    public List<MenuItem> getMenuItemsByRestaurant(int restaurantId) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restaurantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getString("name"),
                        rs.getDouble("price")
                    );
                    item.setId(rs.getInt("id"));
                    item.setRestaurantId(rs.getInt("restaurant_id"));
                    item.setDescription(rs.getString("description"));
                    items.add(item);
                }
            }
            auditService.logAction("READ_RESTAURANT_MENU_ITEMS");
        }
        return items;
    }
} 