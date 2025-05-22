package fooddelivery.service;

import fooddelivery.model.*;

import java.util.*;

public class DeliveryService {
    private List<User> users = new ArrayList<>();
    private Set<MenuItem> menuItems = new TreeSet<>();
    private Map<Restaurant, List<MenuItem>> menus = new HashMap<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void addRestaurant(Restaurant r) {
        menus.putIfAbsent(r, new ArrayList<>());
    }

    public void addMenuItem(Restaurant r, MenuItem m) {
        menus.computeIfAbsent(r, k -> new ArrayList<>()).add(m);
        menuItems.add(m);
    }

    public List<MenuItem> getMenu(Restaurant r) {
        return menus.getOrDefault(r, new ArrayList<>());
    }
}