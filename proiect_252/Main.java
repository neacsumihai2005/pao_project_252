package fooddelivery;

import fooddelivery.model.*;
import fooddelivery.service.DeliveryService;
import fooddelivery.util.OrderComparatorByPrice;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // === 1. Creare utilizatori și șoferi ===
        User u1 = new User("Ana", "ana@gmail.com");
        User u2 = new User("Mihai", "mihai@yahoo.com");
        Driver d1 = new Driver("George", "Scooter");

        service.addUser(u1);
        service.addUser(u2);

        // === 2. Creare restaurante și meniuri ===
        Restaurant r1 = new Restaurant("Pizza Time");
        Restaurant r2 = new Restaurant("Burger House");

        service.addRestaurant(r1);
        service.addRestaurant(r2);

        service.addMenuItem(r1, new MenuItem("Pizza Margherita", 32.5));
        service.addMenuItem(r1, new MenuItem("Pizza Pepperoni", 38.0));
        service.addMenuItem(r2, new MenuItem("Classic Burger", 28.0));
        service.addMenuItem(r2, new MenuItem("Cheese Burger", 31.0));

        // === 3. Vizualizare meniu restaurant ===
        System.out.println("\n=== Meniu Pizza Time ===");
        service.getMenu(r1).forEach(System.out::println);

        System.out.println("\n=== Meniu Burger House ===");
        service.getMenu(r2).forEach(System.out::println);

        // === 4. Creare comenzi ===
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

        // === 5. Afișare comenzi și prețuri ===
        System.out.println("\n=== Detalii comenzi ===");
        System.out.println(o1);
        System.out.println(o2);

        // === 6. Livrare comenzi ===
        Delivery livrare1 = new Delivery(d1, o1);
        System.out.println("\n=== Livrare ===");
        System.out.println(livrare1);

        // === 7. Recenzie restaurante ===
        Review recenzie1 = new Review(u1, r1, 5, "Pizza excelentă!");
        Review recenzie2 = new Review(u2, r2, 4, "Burgeri foarte buni, dar puțin reci.");

        System.out.println("\n=== Recenzii ===");
        System.out.println(recenzie1);
        System.out.println(recenzie2);

        // === 8. Sortare comenzi după preț ===
        List<Order> toateComenzile = new ArrayList<>(List.of(o1, o2));
        toateComenzile.sort(new OrderComparatorByPrice());

        System.out.println("\n=== Comenzi sortate după preț ===");
        toateComenzile.forEach(System.out::println);
    }
}
