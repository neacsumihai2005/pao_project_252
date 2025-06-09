package proiect_252.model;

public class Delivery {
    private int id;
    private Driver driver;
    private Order order;
    private String status;

    public Delivery(Driver driver, Order order) {
        this.driver = driver;
        this.order = order;
        this.status = "PENDING";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Delivery{" +
                "driver=" + driver +
                ", order=" + order +
                ", status='" + status + '\'' +
                '}';
    }
}