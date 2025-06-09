package proiect_252.model;

import java.util.List;

public class Order {
    private int id;
    private User user;
    private List<MenuItem> items;
    private Address deliveryAddress;
    private double totalAmount;

    public Order(User user, List<MenuItem> items, Address deliveryAddress) {
        this.user = user;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal() {
        return items.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { 
        this.items = items;
        this.totalAmount = calculateTotal();
    }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(Address deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", items=" + items +
                ", totalAmount=" + totalAmount +
                '}';
    }
}