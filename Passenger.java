/**
 * Represents a passenger user in the MRT scheduling system.
 * This class extends the User class and provides passenger-specific functionality
 * for accessing the scheduling system to view train schedules.
 * Demonstrates inheritance and polymorphism.
 */
import java.util.Scanner;

public class Passenger extends User {
    /**
     * Constructor for creating a new passenger user.
     * @param name The passenger's name
     */
    public Passenger(String name) {
        super(name);
    }

    /**
     * Returns a string representation of the passenger.
     * @return A formatted string containing the passenger's name
     */
    @Override
    public String toString() {
        return "Passenger: " + getName();
    }

    /**
     * Provides the passenger with access to the scheduling system.
     * This method is called when a passenger logs in and handles
     * all passenger-specific operations through the system.
     * @param scheduleSystem The scheduling system instance
     * @param sc Scanner for user input
     */
    @Override
    public void accessSystem(SchedulingSystem scheduleSystem, Scanner sc) {
        scheduleSystem.passengerMenu(sc);
    }
}
