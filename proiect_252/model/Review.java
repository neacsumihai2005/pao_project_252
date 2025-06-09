package proiect_252.model;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private User user;
    private Restaurant restaurant;
    private int rating; // 1 to 5
    private String comment;
    private LocalDateTime reviewDate;

    public Review() {
        this.reviewDate = LocalDateTime.now();
    }

    public Review(User user, Restaurant restaurant, int rating, String comment) {
        this.user = user;
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }

    @Override
    public String toString() {
        return String.format("Review #%d\nUser: %s\nRestaurant: %s\nRating: %d/5\nComment: %s\nDate: %s",
                id,
                user != null ? user.getName() : "Unknown",
                restaurant != null ? restaurant.getName() : "Unknown",
                rating,
                comment != null ? comment : "No comment",
                reviewDate != null ? reviewDate.toString() : "Unknown");
    }
}