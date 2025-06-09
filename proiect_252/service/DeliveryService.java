package proiect_252.service;

import proiect_252.model.*;
import java.util.*;

public class DeliveryService {
    private List<User> users;
    private List<Restaurant> restaurants;
    private List<Driver> drivers;
    private List<Order> orders;
    private List<Delivery> deliveries;
    private List<Review> reviews;

    public DeliveryService() {
        this.users = new ArrayList<>();
        this.restaurants = new ArrayList<>();
        this.drivers = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.deliveries = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public void addMenuItem(Restaurant restaurant, MenuItem item) {
        restaurant.getMenu().add(item);
    }

    public List<MenuItem> getMenu(Restaurant restaurant) {
        return restaurant.getMenu();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<User> getUsers() { return users; }
    public List<Restaurant> getRestaurants() { return restaurants; }
    public List<Driver> getDrivers() { return drivers; }
    public List<Order> getOrders() { return orders; }
    public List<Delivery> getDeliveries() { return deliveries; }
    public List<Review> getReviews() { return reviews; }
}