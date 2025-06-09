import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SchedulingSystem scheduleSystem = new SchedulingSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWelcome to DIMSUM 'Digital Interactive MRT Schedule Update Manager'");
        System.out.print("Are you a Passenger or Manager? (Enter 'p' or 'm'): ");
        String role = sc.nextLine().trim().toLowerCase();

        if (role.equals("p")) {
            Passenger user = new Passenger("Guest");
            user.accessSystem(scheduleSystem, sc);
        } else if (role.equals("m")) {
            System.out.println("Enter Password for Manager Access: ");
            String password = sc.nextLine().trim();
            if (!password.equals("eatdimsumeveryday")) {
                System.out.println("Invalid password. Exiting.");
                sc.close();
                return;
            }
            Manager user = new Manager("Admin", "M001");
            user.accessSystem(scheduleSystem, sc);
        } else {
            System.out.println("Invalid role. Exiting.");
        }
        sc.close();
    }
}
