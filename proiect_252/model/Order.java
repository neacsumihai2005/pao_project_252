package fooddelivery.model;

import java.util.List;

public class Order {
    private User user;
    private List<MenuItem> items;
    private Address deliveryAddress;

    public Order(User user, List<MenuItem> items, Address deliveryAddress) {
        this.user = user;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
    }

    public User getUser() {
        return user;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public double getTotal() {
        return items.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    @Override
    public String toString() {
        return "Order by " + user.getName() + ", Total: " + getTotal() + " RON";
    }
}