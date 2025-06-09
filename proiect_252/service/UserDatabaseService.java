package proiect_252.service;

import proiect_252.model.User;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseService implements DatabaseService<User> {
    private static UserDatabaseService instance;
    private final DatabaseConfig dbConfig;
    private final AuditService auditService;
    
    private UserDatabaseService() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.auditService = AuditService.getInstance();
    }
    
    public static UserDatabaseService getInstance() {
        if (instance == null) {
            instance = new UserDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void create(User user) throws SQLException {
        int addressId = -1;
        if (user.getAddress() != null) {
            AddressDatabaseService addressService = AddressDatabaseService.getInstance();
            if (user.getAddress().getId() == 0) {
                addressService.create(user.getAddress());
            }
            addressId = user.getAddress().getId();
        }
        String sql = "INSERT INTO users (name, email, address_id) VALUES (?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            if (addressId > 0) {
                pstmt.setInt(3, addressId);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            auditService.logAction("CREATE_USER");
        }
    }
    
    @Override
    public User read(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    int addressId = rs.getInt("address_id");
                    if (addressId > 0) {
                        AddressDatabaseService addressService = AddressDatabaseService.getInstance();
                        user.setAddress(addressService.read(addressId));
                    }
                    auditService.logAction("READ_USER");
                    return user;
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getId());
            pstmt.executeUpdate();
            auditService.logAction("UPDATE_USER");
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_USER");
        }
    }
    
    @Override
    public List<User> readAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            AddressDatabaseService addressService = AddressDatabaseService.getInstance();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                int addressId = rs.getInt("address_id");
                if (addressId > 0) {
                    user.setAddress(addressService.read(addressId));
                }
                users.add(user);
            }
            auditService.logAction("READ_ALL_USERS");
        }
        return users;
    }
} 