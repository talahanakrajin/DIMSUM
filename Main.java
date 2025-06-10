import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SchedulingSystem scheduleSystem = new SchedulingSystem();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to DIMSUM 'Digital Interactive MRT Schedule Update Manager'");
            System.out.print("Are you a Passenger or Manager? (Enter ['p' for Passenger] or ['m' for Manager], or 'exit' to quit): ");
            String role = sc.nextLine().trim().toLowerCase();

            if (role.equals("p")) {
                Passenger user = new Passenger("Guest");
                user.accessSystem(scheduleSystem, sc);
            } else if (role.equals("m")) {
                System.out.println("Enter Password for Manager Access: ");
                String password = sc.nextLine().trim();
                if (!scheduleSystem.checkManagerPassword(password)) {
                    System.out.println("Invalid password. Returning to main menu.");
                    continue;
                }
                Manager user = new Manager("Admin", "M001");
                user.accessSystem(scheduleSystem, sc);
            } else if (role.equals("exit")) {
                break;
            } else {
                System.out.println("Invalid role. Please enter 'p', 'm', or 'exit'.");
            }
        }
        System.out.println("Exiting program. Goodbye!");
        sc.close();
    }

    // PROBLEMS IN DOING METHODS AFTER SIMULATING THE TRAIN RUNNING
    // SHOULD ASK FOR DEPARTURE TIME FOR RESCHEDULE/DELAY/CANCEL
}
