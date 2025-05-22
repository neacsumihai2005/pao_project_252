package fooddelivery.model;

public class Driver extends Person {
    private String vehicle;

    public Driver(String name, String vehicle) {
        super(name);
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}