/**
 * Represents a manager user in the MRT scheduling system.
 * This class extends the User class and provides manager-specific functionality
 * for accessing and managing the scheduling system.
 * Demonstrates inheritance and polymorphism.
 */
public class Manager extends User {
    /**
     * Constructor for creating a new manager user.
     * @param name The manager's name
     * @param id The manager's unique identifier
     */
    public Manager(String name, String id) {
        super(name, id);
    }

    /**
     * Returns a string representation of the manager.
     * @return A formatted string containing the manager's name and ID
     */
    @Override
    public String toString() {
        return "Manager{" +
                "name='" + getName() + '\'' +
                ", id='" + getId() + '\'' +
                '}';
    }

    /**
     * Provides the manager with access to the scheduling system.
     * This method is called when a manager logs in and handles
     * all manager-specific operations through the system.
     * @param manager The scheduling system instance
     * @param sc Scanner for user input
     */
    @Override
    public void accessSystem(SchedulingSystem manager, java.util.Scanner sc) {
        manager.managerMenu(sc);
    }
}
