package proiect_252.service;

import proiect_252.model.Order;
import proiect_252.model.User;
import proiect_252.model.MenuItem;
import proiect_252.model.Address;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseService implements DatabaseService<Order> {
    private static OrderDatabaseService instance;
    private final DatabaseConfig dbConfig;
    private final AuditService auditService;
    private final UserDatabaseService userService;
    private final MenuItemDatabaseService menuItemService;
    private final AddressDatabaseService addressService;
    
    private OrderDatabaseService() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.auditService = AuditService.getInstance();
        this.userService = UserDatabaseService.getInstance();
        this.menuItemService = MenuItemDatabaseService.getInstance();
        this.addressService = AddressDatabaseService.getInstance();
    }
    
    public static OrderDatabaseService getInstance() {
        if (instance == null) {
            instance = new OrderDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void create(Order order) throws SQLException {
        Connection conn = null;
        try {
            conn = dbConfig.getConnection();
            conn.setAutoCommit(false);
            
            // Insert order
            String orderSql = "INSERT INTO orders (user_id, address_id, total_amount) VALUES (?, ?, ?)";
            int orderId;
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, order.getUser().getId());
                pstmt.setInt(2, order.getDeliveryAddress().getId());
                pstmt.setDouble(3, order.getTotalAmount());
                pstmt.executeUpdate();
                
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                        order.setId(orderId);
                    } else {
                        throw new SQLException("Failed to get generated order ID");
                    }
                }
            }
            
            // Insert order items
            String itemSql = "INSERT INTO order_items (order_id, menu_item_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(itemSql)) {
                for (MenuItem item : order.getItems()) {
                    pstmt.setInt(1, orderId);
                    pstmt.setInt(2, item.getId());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            
            conn.commit();
            auditService.logAction("CREATE_ORDER");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Order read(int id) throws SQLException {
        String sql = "SELECT o.*, u.id as user_id, u.name as user_name, u.email as user_email, " +
                    "a.id as address_id, a.street, a.city, a.state, a.zip_code " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.id " +
                    "JOIN addresses a ON o.address_id = a.id " +
                    "WHERE o.id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Create user
                    User user = new User(
                        rs.getString("user_name"),
                        rs.getString("user_email")
                    );
                    user.setId(rs.getInt("user_id"));
                    
                    // Create address
                    Address address = new Address(
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("zip_code")
                    );
                    address.setId(rs.getInt("address_id"));
                    address.setState(rs.getString("state"));
                    
                    // Create order
                    Order order = new Order(user, new ArrayList<>(), address);
                    order.setId(rs.getInt("id"));
                    
                    // Get order items
                    String itemSql = "SELECT mi.* FROM order_items oi " +
                                   "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                                   "WHERE oi.order_id = ?";
                    try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                        itemStmt.setInt(1, id);
                        try (ResultSet itemRs = itemStmt.executeQuery()) {
                            List<MenuItem> items = new ArrayList<>();
                            while (itemRs.next()) {
                                MenuItem item = new MenuItem(
                                    itemRs.getString("name"),
                                    itemRs.getDouble("price")
                                );
                                item.setId(itemRs.getInt("id"));
                                item.setDescription(itemRs.getString("description"));
                                items.add(item);
                            }
                            order.setItems(items);
                        }
                    }
                    
                    auditService.logAction("READ_ORDER");
                    return order;
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(Order order) throws SQLException {
        Connection conn = null;
        try {
            conn = dbConfig.getConnection();
            conn.setAutoCommit(false);
            
            // Update order
            String orderSql = "UPDATE orders SET user_id = ?, address_id = ?, total_amount = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql)) {
                pstmt.setInt(1, order.getUser().getId());
                pstmt.setInt(2, order.getDeliveryAddress().getId());
                pstmt.setDouble(3, order.getTotalAmount());
                pstmt.setInt(4, order.getId());
                pstmt.executeUpdate();
            }
            
            // Delete existing order items
            String deleteSql = "DELETE FROM order_items WHERE order_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, order.getId());
                pstmt.executeUpdate();
            }
            
            // Insert new order items
            String itemSql = "INSERT INTO order_items (order_id, menu_item_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(itemSql)) {
                for (MenuItem item : order.getItems()) {
                    pstmt.setInt(1, order.getId());
                    pstmt.setInt(2, item.getId());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            
            conn.commit();
            auditService.logAction("UPDATE_ORDER");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_ORDER");
        }
    }
    
    @Override
    public List<Order> readAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id FROM orders";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = read(rs.getInt("id"));
                if (order != null) {
                    orders.add(order);
                }
            }
            auditService.logAction("READ_ALL_ORDERS");
        }
        return orders;
    }
} 