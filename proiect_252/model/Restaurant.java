package proiect_252.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private Address address;
    private List<MenuItem> menu;

    public Restaurant() {
        this.menu = new ArrayList<>();
    }

    public Restaurant(String name) {
        this();
        this.name = name;
    }

    public Restaurant(String name, Address address) {
        this(name);
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public List<MenuItem> getMenu() { return menu; }
    public void setMenu(List<MenuItem> menu) { this.menu = menu; }

    @Override
    public String toString() {
        return String.format("Restaurant #%d: %s\nAddress: %s", 
            id, 
            name, 
            address != null ? address.toString() : "No address");
    }
}