package SourceCode;
/**
 * Abstract base class for system users in the MRT scheduling system.
 * This class defines the common properties and behaviors for all users,
 * including both passengers and managers. It provides the foundation for
 * role-based access control and system interaction.
 * Demonstrates use of inheritance and encapsulation.
 */
public abstract class User {
    /** The user's name */
    private final String name;
    
    /** The user's unique identifier (may be null for passengers) */
    private final String id;

    /**
     * Constructor for creating a user with both name and ID.
     * @param name The user's name
     * @param id The user's unique identifier
     */
    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Constructor for creating a user with only a name.
     * Used primarily for passenger users who don't need an ID.
     * @param name The user's name
     */
    public User(String name) {
        this.name = name;
        this.id = null;
    }

    /**
     * Gets the user's name.
     * @return The user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's ID.
     * @return The user's unique identifier, or null if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Abstract method for accessing the scheduling system.
     * Must be implemented by concrete user classes to provide
     * role-specific system access and functionality.
     * @param scheduleSystem The scheduling system to access
     * @param sc Scanner for user input
     */
    public abstract void accessSystem(SchedulingSystem scheduleSystem, java.util.Scanner sc);
}
