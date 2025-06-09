package proiect_252.service;

import proiect_252.model.Address;
import proiect_252.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDatabaseService implements DatabaseService<Address> {
    private static AddressDatabaseService instance;
    private final DatabaseConfig dbConfig;
    private final AuditService auditService;

    private AddressDatabaseService() {
        this.dbConfig = DatabaseConfig.getInstance();
        this.auditService = AuditService.getInstance();
    }

    public static AddressDatabaseService getInstance() {
        if (instance == null) {
            instance = new AddressDatabaseService();
        }
        return instance;
    }

    @Override
    public void create(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (street, city, state, zip_code) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, address.getStreet());
            pstmt.setString(2, address.getCity());
            pstmt.setString(3, address.getState() != null ? address.getState() : "");
            pstmt.setString(4, address.getZipCode());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    address.setId(generatedKeys.getInt(1));
                }
            }
            auditService.logAction("CREATE_ADDRESS");
        }
    }

    @Override
    public Address read(int id) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address(
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("zip_code")
                    );
                    address.setId(rs.getInt("id"));
                    address.setState(rs.getString("state"));
                    auditService.logAction("READ_ADDRESS");
                    return address;
                }
            }
        }
        return null;
    }

    @Override
    public void update(Address address) throws SQLException {
        String sql = "UPDATE addresses SET street = ?, city = ?, state = ?, zip_code = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address.getStreet());
            pstmt.setString(2, address.getCity());
            pstmt.setString(3, address.getState() != null ? address.getState() : "");
            pstmt.setString(4, address.getZipCode());
            pstmt.setInt(5, address.getId());
            pstmt.executeUpdate();
            auditService.logAction("UPDATE_ADDRESS");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        // First, update any users that reference this address
        String updateUsersSql = "UPDATE users SET address_id = NULL WHERE address_id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateUsersSql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

        // Then delete the address
        String deleteAddressSql = "DELETE FROM addresses WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteAddressSql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_ADDRESS");
        }
    }

    @Override
    public List<Address> readAll() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Address address = new Address(
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getString("zip_code")
                );
                address.setId(rs.getInt("id"));
                address.setState(rs.getString("state"));
                addresses.add(address);
            }
            auditService.logAction("READ_ALL_ADDRESSES");
        }
        return addresses;
    }

    public Address findByDetails(String street, String city, String zipCode) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE street = ? AND city = ? AND zip_code = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, street);
            pstmt.setString(2, city);
            pstmt.setString(3, zipCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address(
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("zip_code")
                    );
                    address.setId(rs.getInt("id"));
                    address.setState(rs.getString("state"));
                    return address;
                }
            }
        }
        return null;
    }
} 