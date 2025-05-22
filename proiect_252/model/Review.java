package fooddelivery.model;

public class Review {
    private User user;
    private Restaurant restaurant;
    private int rating; // 1 to 5
    private String comment;

    public Review(User user, Restaurant restaurant, int rating, String comment) {
        this.user = user;
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return user.getName() + " rated " + restaurant.getName() + ": " + rating + "/5 - " + comment;
    }
}