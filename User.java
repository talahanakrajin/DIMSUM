/**
 * Abstract base class for system users.
 * Demonstrates use of inheritance and encapsulation.
 */
public abstract class User {
    private final String name;
    private final String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public User(String name) {
        this.name = name;
        this.id = null;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

    public abstract void accessSystem(SchedulingSystem scheduleSystem, java.util.Scanner sc);
}
