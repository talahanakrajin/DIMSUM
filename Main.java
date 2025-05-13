import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        /*
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(500, "T001");
        treeMap.put(510, "T002");
        treeMap.put(530, "T004");
        treeMap.put(540, "T005");
        treeMap.put(600, "T006");

        // Display the TreeMap
        System.out.println("TreeMap: " + treeMap);

        Methods methods = new Methods(); 

        methods.addEntry(treeMap, 520, "T003");
        System.out.println("After adding T003: " + treeMap);
        methods.addEntry(treeMap, 530, "T007");
        System.out.println("After adding T007: " + treeMap);

        methods.removeEntry(treeMap, 510);
        System.out.println("After removing T002: " + treeMap);
        methods.removeEntry(treeMap, 600);
        System.out.println("After removing T006: " + treeMap);
        */
        TreeMap<Integer, String> trainMap = new TreeMap<>(); 

        int choice;
        Scanner sc = new Scanner(System.in); // Ensure Scanner is correctly initialized

        System.out.println("\nWelcome to DIMSUM 'Digital Interactive MRT Schedule Update Manager'\n");

        do {
            System.out.println("Please select an option:");
            System.out.println("1. View full schedule");
            System.out.println("2. View each station's schedule");
            System.out.println("3. Find next train arriving");
            System.out.println("4. Add train schedule(s)");
            System.out.println("5. Reschedule train(s)");
            System.out.println("6. Delay train(s)");
            System.out.println("7. Cancel train(s)");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) { // Handles invalid inputs
                System.out.println("Wrong Input! Please enter a number between 1 and 8.");
                sc.next(); // Consume invalid input
                System.out.print("Enter your choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            String trainID = null;
            int departureTime = 0;
            Train train = new Train(trainMap, trainID, departureTime); 

            switch (choice) {
                /* For the users! */
                case 1:
                    System.out.println("Option 1 selected: View full schedule.");
                    System.out.println("\n-- Train Schedule --");
                    train.getTrainMap().forEach((key, value) -> {
                        String keyString = String.format("%04d", key);
                        String hours = keyString.substring(0, 2);
                        String minutes = keyString.substring(2, 4);
                        System.out.printf("Departure Time: %s:%s, Train ID: %s%n\n", hours, minutes, value);
                    });
                    break;
                case 2:
                    System.out.println("Option 2 selected: View each station's schedule.");
                    // Add functionality here
                    break;
                case 3:
                    System.out.println("Option 3 selected: Find next train arriving.");
                    // Add functionality here
                    break;
                     
                /* For the manager! */
                case 4:
                    System.out.println("\nOption 4 selected: Add train schedule(s).\n");
                    // Train ID
                    System.out.print("Enter the train ID (format: TSxxxx): ");
                    train.setTrainID(sc.nextLine().toUpperCase());
                    System.out.println("DEBUG PRINT Train ID: " + train.getTrainID());
                    // Departure time
                    System.out.print("Enter the departure time (format: HHMM): ");
                    train.setDepartureTime(sc.nextInt());
                    System.out.println("DEBUG PRINT departure time: " + train.getDepartureTime());
                    sc.nextLine(); // Consume the newline character
                    train.add_ID_and_departure_time();
                    break;
                case 5:
                    System.out.println("Option 5 selected: Reschedule train(s).");
                    // Add functionality here
                    break;
                case 6:
                    System.out.println("Option 6 selected: Delay train(s).");
                    // Add functionality here
                    break;
                case 7:
                    System.out.println("Option 7 selected: Cancel train(s).");
                    // Add functionality here
                    break;
                case 8:
                    System.out.println("Exiting the Train Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 8.");
            }
        } while (choice != 8);

        sc.close(); 
    }
}
