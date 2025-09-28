package RescueAnimals.Src;

/**
 * Monkey class extends RescueAnimal.
 * Adds monkey-specific attributes in a real system.
 */
public class Monkey extends RescueAnimal {

    // Example of a species-specific field
    private String species;

    /**
     * Constructor for Monkey.
     * @param name   Monkey's name
     * @param status Monkey's status
     */
    public Monkey(String name, String status) {
        super(name, status);
        this.species = "Unknown"; // Default placeholder
    }

    // Getter and setter for species
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * String representation of Monkey.
     */
    @Override
    public String toString() {
        return "Monkey - " + super.toString() + " | Species: " + species;
    }
}
