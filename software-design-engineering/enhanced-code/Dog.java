package RescueAnimals.Src;

/**
 * Dog class extends RescueAnimal.
 * Adds species-specific attributes in a real system.
 */
public class Dog extends RescueAnimal {

    // Example of a species-specific field (can be expanded later)
    private String breed;

    /**
     * Constructor for Dog.
     * @param name   Dog's name
     * @param status Dog's status
     */
    public Dog(String name, String status) {
        super(name, status);
        this.breed = "Unknown"; // Default placeholder
    }

    // Getter and setter for breed
    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }

    /**
     * String representation of Dog.
     */
    @Override
    public String toString() {
        return "Dog - " + super.toString() + " | Breed: " + breed;
    }
}
