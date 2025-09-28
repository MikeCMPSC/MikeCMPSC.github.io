package RescueAnimals.Src;

/**
 * Base class representing a rescue animal.
 * Demonstrates inheritance and encapsulation.
 * Extended by Dog and Monkey classes.
 */
public class RescueAnimal {
    private String name;    // Animal's name
    private String status;  // Availability status (Available / Reserved)

    /**
     * Constructor for RescueAnimal.
     * @param name  The animal's name
     * @param status The availability status
     */
    public RescueAnimal(String name, String status) {
        this.name = name;
        this.status = status;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * String representation of RescueAnimal.
     */
    @Override
    public String toString() {
        return "Name: " + name + " | Status: " + status;
    }
}
