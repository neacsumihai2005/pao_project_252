package proiect_252.service;

import proiect_252.model.Review;
import proiect_252.model.User;
import proiect_252.model.Restaurant;
import proiect_252.util.DatabaseConnection;
import proiect_252.util.AuditService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDatabaseService implements DatabaseService<Review> {
    private static ReviewDatabaseService instance;
    private final AuditService auditService;

    private ReviewDatabaseService() {
        this.auditService = AuditService.getInstance();
    }

    public static ReviewDatabaseService getInstance() {
        if (instance == null) {
            instance = new ReviewDatabaseService();
        }
        return instance;
    }

    @Override
    public void create(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, restaurant_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, review.getUser().getId());
            pstmt.setInt(2, review.getRestaurant().getId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.setTimestamp(5, Timestamp.valueOf(review.getReviewDate()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                }
            }
            
            auditService.logAction("CREATE_REVIEW");
        }
    }

    @Override
    public Review read(int id) throws SQLException {
        String sql = "SELECT r.*, u.id as user_id, u.name as user_name, " +
                    "res.id as restaurant_id, res.name as restaurant_name " +
                    "FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN restaurants res ON r.restaurant_id = res.id " +
                    "WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractReviewFromResultSet(rs);
                }
            }
            
            auditService.logAction("READ_REVIEW");
            return null;
        }
    }

    @Override
    public void update(Review review) throws SQLException {
        String sql = "UPDATE reviews SET user_id = ?, restaurant_id = ?, rating = ?, comment = ?, review_date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, review.getUser().getId());
            pstmt.setInt(2, review.getRestaurant().getId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.setTimestamp(5, Timestamp.valueOf(review.getReviewDate()));
            pstmt.setInt(6, review.getId());
            
            pstmt.executeUpdate();
            auditService.logAction("UPDATE_REVIEW");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reviews WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            auditService.logAction("DELETE_REVIEW");
        }
    }

    @Override
    public List<Review> readAll() throws SQLException {
        String sql = "SELECT r.*, u.id as user_id, u.name as user_name, " +
                    "res.id as restaurant_id, res.name as restaurant_name " +
                    "FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN restaurants res ON r.restaurant_id = res.id";
        
        List<Review> reviews = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
            
            auditService.logAction("READ_ALL_REVIEWS");
            return reviews;
        }
    }

    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setName(rs.getString("user_name"));
        review.setUser(user);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(rs.getInt("restaurant_id"));
        restaurant.setName(rs.getString("restaurant_name"));
        review.setRestaurant(restaurant);
        
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("review_date").toLocalDateTime());
        
        return review;
    }
} 