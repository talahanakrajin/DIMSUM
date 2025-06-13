/**
 * Main entry point for the MRT Scheduling System.
 * This class handles the initial user authentication and role-based access control.
 * It creates the Scheduling System instance and manages the main program loop.
 */
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the core scheduling system
        SchedulingSystem scheduleSystem = new SchedulingSystem();
        Scanner sc = new Scanner(System.in);

        // Main Menu Loop
        while (true) {
            // Display welcome message and prompt for user role
            System.out.println("\nWelcome to DIMSUM 'Digital Interactive MRT Schedule Update Manager'");
            System.out.print("Are you a Passenger or Manager? (Enter ['p' for Passenger] or ['m' for Manager], or 'exit' to quit): ");
            String role = sc.nextLine().trim().toLowerCase();

            // Handle user role selection
            if (role.equals("p")) {
                // Create and initialize passenger user
                Passenger user = new Passenger("Sir Feri Setiawan");
                user.accessSystem(scheduleSystem, sc);
            } else if (role.equals("m")) {
                // Handle manager authentication
                System.out.println("Enter Password for Manager Access: ");
                String password = sc.nextLine().trim();
                if (!scheduleSystem.checkManagerPassword(password)) {
                    System.out.println("Invalid password. Returning to main menu.");
                    continue;
                }
                // Create and initialize manager user
                Manager user = new Manager("Jude Joseph Lamung Martinez", "JJM123");
                user.accessSystem(scheduleSystem, sc);
            } else if (role.equals("exit")) {
                // Exit the program
                break;
            } else {
                // Handle invalid role input
                System.out.println("Invalid role. Please enter 'p', 'm', or 'exit'.");
            }
        }
        // Cleanup and exit
        System.out.println("Exiting program. Goodbye!");
        sc.close();
    }
}
