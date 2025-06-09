package proiect_252.model;

public class Driver {
    private int id;
    private String name;
    private String vehicleType;
    private String licenseNumber;

    public Driver(String name, String vehicleType) {
        this.name = name;
        this.vehicleType = vehicleType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}