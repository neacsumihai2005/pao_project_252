package fooddelivery.model;

public class Delivery {
    private Driver driver;
    private Order order;

    public Delivery(Driver driver, Order order) {
        this.driver = driver;
        this.order = order;
    }

    public Driver getDriver() {
        return driver;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Delivery by " + driver.getName() + " for " + order;
    }
}