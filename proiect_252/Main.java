package proiect_252;

import proiect_252.model.*;
import proiect_252.service.*;
import proiect_252.util.OrderComparatorByPrice;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize services
            DeliveryService service = new DeliveryService();
            RestaurantDatabaseService restaurantService = RestaurantDatabaseService.getInstance();
            AuditService auditService = AuditService.getInstance();

            // === 1. Create users and drivers ===
            User u1 = new User("Ana", "ana@gmail.com");
            User u2 = new User("Mihai", "mihai@yahoo.com");
            Driver d1 = new Driver("George", "Scooter");

            service.addUser(u1);
            service.addUser(u2);
            auditService.logAction("CREATE_USERS_AND_DRIVERS");

            // === 2. Create restaurants and menus ===
            Address r1Address = new Address("Str. Pizzeria", "București", "010101");
            Address r2Address = new Address("Str. Burger", "București", "010102");
            
            Restaurant r1 = new Restaurant("Pizza Time", r1Address);
            Restaurant r2 = new Restaurant("Burger House", r2Address);

            // Save restaurants to database
            restaurantService.create(r1);
            restaurantService.create(r2);
            auditService.logAction("CREATE_RESTAURANTS");

            service.addMenuItem(r1, new MenuItem("Pizza Margherita", 32.5));
            service.addMenuItem(r1, new MenuItem("Pizza Pepperoni", 38.0));
            service.addMenuItem(r2, new MenuItem("Classic Burger", 28.0));
            service.addMenuItem(r2, new MenuItem("Cheese Burger", 31.0));
            auditService.logAction("CREATE_MENU_ITEMS");

            // === 3. View restaurant menus ===
            System.out.println("\n=== Meniu Pizza Time ===");
            service.getMenu(r1).forEach(System.out::println);

            System.out.println("\n=== Meniu Burger House ===");
            service.getMenu(r2).forEach(System.out::println);
            auditService.logAction("VIEW_MENUS");

            // === 4. Create orders ===
            List<MenuItem> comanda1 = List.of(
                    new MenuItem("Pizza Margherita", 32.5),
                    new MenuItem("Pizza Pepperoni", 38.0)
            );
            Address adresa1 = new Address("Str. Libertății", "București", "010101");
            Order o1 = new Order(u1, comanda1, adresa1);

            List<MenuItem> comanda2 = List.of(
                    new MenuItem("Classic Burger", 28.0),
                    new MenuItem("Cheese Burger", 31.0)
            );
            Address adresa2 = new Address("Bd. Tineretului", "București", "020202");
            Order o2 = new Order(u2, comanda2, adresa2);
            auditService.logAction("CREATE_ORDERS");

            // === 5. Display orders and prices ===
            System.out.println("\n=== Detalii comenzi ===");
            System.out.println(o1);
            System.out.println(o2);
            auditService.logAction("VIEW_ORDERS");

            // === 6. Deliver orders ===
            Delivery livrare1 = new Delivery(d1, o1);
            System.out.println("\n=== Livrare ===");
            System.out.println(livrare1);
            auditService.logAction("CREATE_DELIVERY");

            // === 7. Restaurant reviews ===
            Review recenzie1 = new Review(u1, r1, 5, "Pizza excelentă!");
            Review recenzie2 = new Review(u2, r2, 4, "Burgeri foarte buni, dar puțin reci.");
            auditService.logAction("CREATE_REVIEWS");

            System.out.println("\n=== Recenzii ===");
            System.out.println(recenzie1);
            System.out.println(recenzie2);

            // === 8. Sort orders by price ===
            List<Order> toateComenzile = new ArrayList<>(List.of(o1, o2));
            toateComenzile.sort(new OrderComparatorByPrice());
            auditService.logAction("SORT_ORDERS");

            System.out.println("\n=== Comenzi sortate după preț ===");
            toateComenzile.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
