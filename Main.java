import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int choice;
        int stationNumber = 0; // Initialize stationNumber 

        Scanner sc = new Scanner(System.in); // Ensure Scanner is correctly initialized

        // Create Train object ONCE and reuse it
        String trainID = null;
        int departureTime = 0;
        Train train = new Train(trainID, departureTime); 

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

            switch (choice) {
                /* For the users! */
                case 1: // View full schedule
                    System.out.println("Option 1 selected: View full schedule.");
                    System.out.println("\n-- Train Schedule --");
                    train.printSchedule(null);
                    break;
                case 2: // View each station's schedule
                    System.out.println("Option 2 selected: View each station's schedule.");
                    System.out.println("\n-- Station Schedule --");
                    train.getStationMap().forEach((key, value) -> {
                        System.out.println(key + ". " + value + "\n");
                    });
                    System.out.print("Enter the station number to view its schedule: ");

                    stationNumber = train.checkStationNumber(stationNumber);
                    String selectedStation = train.getStationMap().get(stationNumber);

                    System.out.println("You selected station: " + selectedStation);
                    System.out.println("Schedule for " + selectedStation + ":");

                    train.printSchedule(selectedStation);
                    stationNumber = 0; // Reset stationNumber for next input                    
                    break;
                case 3: // Find next train arriving
                    System.out.println("Option 3 selected: Find next train arriving.");
                    // Add functionality here
                    break;

                /* For the manager! */
                case 4: // Add train schedule(s)
                    System.out.println("\nOption 4 selected: Add train schedule(s).\n");
                    System.out.println("Please enter the following details to add a new train schedule:\nWhich station do you want to add a train schedule to?\n\n1. Lebak Bulus (Start Terminus)\n2. Bundaran HI (End Terminus)\n3. Blok M)");
                    
                    boolean validInput = false;
                    String stationName = null;
                    while (!validInput) {
                        if (sc.hasNextInt()) {
                            stationNumber = sc.nextInt();
                            if (stationNumber < 1 || stationNumber > 3) {
                                System.out.print("Invalid station number! Please enter a number between 1 - 3: ");
                            } else {
                                validInput = true;
                            }
                        } else {
                            System.out.print("Invalid input! Please enter a number between 1 - 3: ");
                            sc.next(); // Consume invalid input
                        }
                    }
                    
                    switch (stationNumber) {
                        case 1:
                            System.out.println("You selected station: Lebak Bulus (Start Terminus)");
                            stationName = "Lebak Bulus";
                            break;
                        case 2:
                            System.out.println("You selected station: Bundaran HI (End Terminus)");
                            stationName = "Bundaran HI";
                            break;
                        case 3:
                            System.out.println("You selected station: Blok M");
                            stationName = "Blok M";
                            break;
                    }
                    sc.nextLine(); // Consume the newline character
                    System.out.println("Please enter the following details to add a new train schedule:\n");

                    // Train ID
                    train.inputTrainID(sc);
                    System.out.println("DEBUG PRINT Train ID: " + train.getTrainID());
                    // Departure time
                    train.inputDepartureTime(sc);
                    System.out.println("DEBUG PRINT departure time: " + train.getDepartureTime());
                    
                    train.addToScheduleMap(train.getDepartureTime(), train.getTrainID(), stationName);
                    System.out.println("Train schedule added successfully!");
                    break;
                case 5: // Reschedule train(s)
                    System.out.println("Option 5 selected: Reschedule train(s).");
                    // Add functionality here
                    break;
                case 6: // Delay train(s)
                    System.out.println("Option 6 selected: Delay train(s).");
                    // Add functionality here
                    break;
                case 7: // Cancel train(s)
                    System.out.println("Option 7 selected: Cancel train(s).");
                    // Add functionality here
                    break;
                case 8: // Exit
                    System.out.println("Exiting the Train Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 8.");
            }
        } while (choice != 8); // while the user does not select 8 (exit) do the switch statements

        sc.close(); 
    }
}
