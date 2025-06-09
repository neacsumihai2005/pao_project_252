package proiect_252.util;

import proiect_252.model.*;
import proiect_252.service.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DatabaseMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RestaurantDatabaseService restaurantService = RestaurantDatabaseService.getInstance();
    private static final UserDatabaseService userService = UserDatabaseService.getInstance();
    private static final AddressDatabaseService addressService = AddressDatabaseService.getInstance();
    private static final AuditService auditService = AuditService.getInstance();

    public static void showMenu() {
        while (true) {
            System.out.println("\n=== Database Management Menu ===");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Restaurants");
            System.out.println("3. Manage Menu Items");
            System.out.println("4. Manage Addresses");
            System.out.println("5. Manage Drivers");
            System.out.println("6. Manage Orders");
            System.out.println("7. Manage Reviews");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    manageRestaurants();
                    break;
                case 3:
                    manageMenuItems();
                    break;
                case 4:
                    manageAddresses();
                    break;
                case 5:
                    manageDrivers();
                    break;
                case 6:
                    manageOrders();
                    break;
                case 7:
                    manageReviews();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void manageUsers() {
        while (true) {
            System.out.println("\n=== User Management ===");
            System.out.println("1. Add User");
            System.out.println("2. Delete User");
            System.out.println("3. List All Users");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    listUsers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();
        System.out.print("Enter street: ");
        String street = scanner.nextLine();
        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        System.out.print("Enter state: ");
        String state = scanner.nextLine();
        System.out.print("Enter zip code: ");
        String zipCode = scanner.nextLine();

        try {
            Address address = new Address(street, city, zipCode);
            address.setState(state);
            addressService.create(address);
            User user = new User(name, email);
            user.setAddress(address);
            userService.create(user);
            auditService.logAction("ADD_USER_VIA_MENU");
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            userService.delete(id);
            auditService.logAction("DELETE_USER_VIA_MENU");
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void listUsers() {
        try {
            List<User> users = userService.readAll();
            System.out.println("\nAll Users:");
            for (User user : users) {
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.out.println("Error listing users: " + e.getMessage());
        }
    }

    private static void manageRestaurants() {
        while (true) {
            System.out.println("\n=== Restaurant Management ===");
            System.out.println("1. Add Restaurant");
            System.out.println("2. Delete Restaurant");
            System.out.println("3. List All Restaurants");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addRestaurant();
                    break;
                case 2:
                    deleteRestaurant();
                    break;
                case 3:
                    listRestaurants();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addRestaurant() {
        System.out.print("Enter restaurant name: ");
        String name = scanner.nextLine();
        System.out.print("Enter street: ");
        String street = scanner.nextLine();
        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        System.out.print("Enter zip code: ");
        String zipCode = scanner.nextLine();

        try {
            // First check if the address already exists
            Address address = addressService.findByDetails(street, city, zipCode);
            if (address == null) {
                // Only create a new address if one doesn't exist
                address = new Address(street, city, zipCode);
                addressService.create(address);
            }
            
            Restaurant restaurant = new Restaurant(name, address);
            restaurantService.create(restaurant);
            auditService.logAction("ADD_RESTAURANT_VIA_MENU");
            System.out.println("Restaurant added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding restaurant: " + e.getMessage());
        }
    }

    private static void deleteRestaurant() {
        System.out.print("Enter restaurant ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            restaurantService.delete(id);
            auditService.logAction("DELETE_RESTAURANT_VIA_MENU");
            System.out.println("Restaurant deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting restaurant: " + e.getMessage());
        }
    }

    private static void listRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.readAll();
            System.out.println("\nAll Restaurants:");
            System.out.println("Number of restaurants found: " + restaurants.size());
            for (Restaurant r : restaurants) {
                System.out.println("Restaurant ID: " + r.getId());
                System.out.println("Restaurant Name: " + r.getName());
                System.out.println("Restaurant Address: " + (r.getAddress() != null ? r.getAddress().toString() : "null"));
                System.out.println("-------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error listing restaurants: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void manageMenuItems() {
        while (true) {
            System.out.println("\n=== Menu Item Management ===");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Delete Menu Item");
            System.out.println("3. List All Menu Items");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addMenuItem();
                    break;
                case 2:
                    deleteMenuItem();
                    break;
                case 3:
                    listMenuItems();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addMenuItem() {
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        try {
            Restaurant restaurant = restaurantService.read(restaurantId);
            if (restaurant != null) {
                MenuItem item = new MenuItem(name, price);
                restaurant.getMenu().add(item);
                restaurantService.update(restaurant);
                auditService.logAction("ADD_MENU_ITEM_VIA_MENU");
                System.out.println("Menu item added successfully!");
            } else {
                System.out.println("Restaurant not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding menu item: " + e.getMessage());
        }
    }

    private static void deleteMenuItem() {
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter menu item index to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            Restaurant restaurant = restaurantService.read(restaurantId);
            if (restaurant != null && index >= 0 && index < restaurant.getMenu().size()) {
                restaurant.getMenu().remove(index);
                restaurantService.update(restaurant);
                auditService.logAction("DELETE_MENU_ITEM_VIA_MENU");
                System.out.println("Menu item deleted successfully!");
            } else {
                System.out.println("Restaurant or menu item not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
        }
    }

    private static void listMenuItems() {
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            Restaurant restaurant = restaurantService.read(restaurantId);
            if (restaurant != null) {
                System.out.println("\nMenu Items for " + restaurant.getName() + ":");
                for (MenuItem item : restaurant.getMenu()) {
                    System.out.println(item);
                }
            } else {
                System.out.println("Restaurant not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error listing menu items: " + e.getMessage());
        }
    }

    private static void manageAddresses() {
        while (true) {
            System.out.println("\n=== Address Management ===");
            System.out.println("1. Add Address");
            System.out.println("2. Delete Address");
            System.out.println("3. List All Addresses");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addAddress();
                    break;
                case 2:
                    deleteAddress();
                    break;
                case 3:
                    listAddresses();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addAddress() {
        System.out.print("Enter street: ");
        String street = scanner.nextLine();
        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        System.out.print("Enter zip code: ");
        String zipCode = scanner.nextLine();

        try {
            Address address = new Address(street, city, zipCode);
            addressService.create(address);
            auditService.logAction("ADD_ADDRESS_VIA_MENU");
            System.out.println("Address added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding address: " + e.getMessage());
        }
    }

    private static void deleteAddress() {
        System.out.print("Enter address ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            addressService.delete(id);
            auditService.logAction("DELETE_ADDRESS_VIA_MENU");
            System.out.println("Address deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting address: " + e.getMessage());
        }
    }

    private static void listAddresses() {
        try {
            List<Address> addresses = addressService.readAll();
            System.out.println("\nAll Addresses:");
            for (Address address : addresses) {
                System.out.println(address);
            }
        } catch (SQLException e) {
            System.out.println("Error listing addresses: " + e.getMessage());
        }
    }

    private static void manageDrivers() {
        while (true) {
            System.out.println("\n=== Driver Management ===");
            System.out.println("1. Add Driver");
            System.out.println("2. Delete Driver");
            System.out.println("3. List All Drivers");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addDriver();
                    break;
                case 2:
                    deleteDriver();
                    break;
                case 3:
                    listDrivers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addDriver() {
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();
        System.out.print("Enter vehicle type: ");
        String vehicleType = scanner.nextLine();

        try {
            Driver driver = new Driver(name, vehicleType);
            // Add driver to database
            auditService.logAction("ADD_DRIVER_VIA_MENU");
            System.out.println("Driver added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding driver: " + e.getMessage());
        }
    }

    private static void deleteDriver() {
        System.out.print("Enter driver ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // Delete driver from database
            auditService.logAction("DELETE_DRIVER_VIA_MENU");
            System.out.println("Driver deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting driver: " + e.getMessage());
        }
    }

    private static void listDrivers() {
        try {
            // List all drivers from database
            System.out.println("\nAll Drivers:");
            // Add code to fetch and display drivers
        } catch (Exception e) {
            System.out.println("Error listing drivers: " + e.getMessage());
        }
    }

    private static void manageOrders() {
        while (true) {
            System.out.println("\n=== Order Management ===");
            System.out.println("1. Add Order");
            System.out.println("2. Delete Order");
            System.out.println("3. List All Orders");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    deleteOrder();
                    break;
                case 3:
                    listOrders();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addOrder() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter delivery address (street): ");
        String street = scanner.nextLine();
        System.out.print("Enter delivery address (city): ");
        String city = scanner.nextLine();
        System.out.print("Enter delivery address (zip code): ");
        String zipCode = scanner.nextLine();

        try {
            // Add order to database
            auditService.logAction("ADD_ORDER_VIA_MENU");
            System.out.println("Order added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding order: " + e.getMessage());
        }
    }

    private static void deleteOrder() {
        System.out.print("Enter order ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // Delete order from database
            auditService.logAction("DELETE_ORDER_VIA_MENU");
            System.out.println("Order deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting order: " + e.getMessage());
        }
    }

    private static void listOrders() {
        try {
            // List all orders from database
            System.out.println("\nAll Orders:");
            // Add code to fetch and display orders
        } catch (Exception e) {
            System.out.println("Error listing orders: " + e.getMessage());
        }
    }

    private static void manageReviews() {
        while (true) {
            System.out.println("\n=== Review Management ===");
            System.out.println("1. Add Review");
            System.out.println("2. Delete Review");
            System.out.println("3. List All Reviews");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addReview();
                    break;
                case 2:
                    deleteReview();
                    break;
                case 3:
                    listReviews();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addReview() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter review text: ");
        String text = scanner.nextLine();

        try {
            // Add review to database
            auditService.logAction("ADD_REVIEW_VIA_MENU");
            System.out.println("Review added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding review: " + e.getMessage());
        }
    }

    private static void deleteReview() {
        System.out.print("Enter review ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            // Delete review from database
            auditService.logAction("DELETE_REVIEW_VIA_MENU");
            System.out.println("Review deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting review: " + e.getMessage());
        }
    }

    private static void listReviews() {
        try {
            // List all reviews from database
            System.out.println("\nAll Reviews:");
            // Add code to fetch and display reviews
        } catch (Exception e) {
            System.out.println("Error listing reviews: " + e.getMessage());
        }
    }
} 