import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int choice;
        int stationNumber = 0; // Initialize stationNumber 

        Scanner sc = new Scanner(System.in); // Ensure Scanner is correctly initialized

        // Create Train object ONCE and reuse it
        String trainID = null;
        int departureTime = 0;
        int delayMinutes = 0; 

        boolean validInput = false; // Flag for valid input
        Train train = new Train(trainID, departureTime); 

        System.out.println("\nWelcome to DIMSUM 'Digital Interactive MRT Schedule Update Manager'\n");

        do {
            System.out.println("Please select an option:");
            System.out.println("1. View full schedule");
            System.out.println("2. View each station's schedule");
            System.out.println("3. Find next train departing");
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
                case 3: // Find next train departing
                    System.out.println("Option 3 selected: Find next departing train.");

                    int nextDepTime = train.getScheduleMap().firstEntry().getKey(); // Get the first entry (earliest departure time)
                    String[] parts = train.getScheduleMap().firstEntry().getValue().split(",", 2); // List of parts: [TrainID, StationName]
                    String nextTrainID = parts[0]; // Train ID
                    String nextStationName = parts.length > 1 ? parts[1] : ""; // Station name

                    String nextDepTimeStr = String.format("%04d", nextDepTime);
                    String hours = nextDepTimeStr.substring(0, 2);
                    String minutes = nextDepTimeStr.substring(2, 4);

                    System.out.printf("Next train departing:\nTrain ID: %s | Departure Time: %s:%s | Station: %s%n", nextTrainID, hours, minutes, nextStationName);
                    break;

                /* For the manager! */
                case 4: // Add train schedule(s)
                    System.out.println("\nOption 4 selected: Add train schedule(s).\n");
                    System.out.print("How many schedules do you want to add? ");
                    int addCount = 1;
                    while (true) {
                        if (sc.hasNextInt()) {
                            addCount = sc.nextInt();
                            if (addCount > 0) break;
                            else System.out.print("Please enter a positive number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < addCount; i++) {
                        System.out.println("\nPlease enter the following details to add a new train schedule:\nWhich station do you want to add a train schedule to?\n\n1. Lebak Bulus (Start Terminus)\n2. Bundaran HI (End Terminus)\n3. Blok M)");
                        validInput = false;
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
                        int depTime = train.inputDepTime(sc);
                        System.out.println("DEBUG PRINT departure time: " + depTime);

                        train.addToScheduleMap(depTime, train.getTrainID(), stationName);
                        System.out.println("Train schedule added successfully!");
                    }
                    break;
                
                case 5: // Reschedule train(s)
                    System.out.println("Option 5 selected: Reschedule train(s).");
                    System.out.print("How many schedules do you want to reschedule? ");
                    int rescheduleCount = 1;
                    while (true) {
                        if (sc.hasNextInt()) {
                            rescheduleCount = sc.nextInt();
                            if (rescheduleCount > 0) break;
                            else System.out.print("Please enter a positive number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < rescheduleCount; i++) {
                        System.out.println("Find the train using\n1. Train ID?\n2. Departure time?\nSelect the option: ");
                        int rescheduleOption = 0;
                        validInput = false;
                        while (!validInput) {
                            if (sc.hasNextInt()) {
                                rescheduleOption = sc.nextInt();
                                if (rescheduleOption == 1 || rescheduleOption == 2) {
                                    validInput = true;
                                } else {
                                    System.out.print("Invalid option! Please enter 1 or 2: ");
                                }
                            } else {
                                System.out.print("Invalid input! Please enter 1 or 2: ");
                                sc.next();
                            }
                        }
                        sc.nextLine(); // Consume the newline character

                        switch (rescheduleOption) {
                            case 1:
                                System.out.println("You selected option 1: Train ID.");
                                System.out.print("Enter the train ID to reschedule: ");
                                String rescheduleTrainID = sc.nextLine().toUpperCase();
                                int newDepartureTime = train.inputDepTime(sc);

                                int rescheduleStationNumber = train.inputStation(sc);
                                String rescheduleStationName = train.getStationMap().get(rescheduleStationNumber);

                                // Call train method to perform the rescheduling
                                train.rescheduleTrain(rescheduleTrainID, newDepartureTime, rescheduleStationName);
                                break;
                            case 2:
                                System.out.println("You selected option 2: Departure time.");
                                System.out.print("Enter the departure time to reschedule (format: HHMM): ");
                                int oldDepartureTime = train.inputDepTime(sc);

                                System.out.print("Enter the new departure time (format: HHMM): ");
                                int newDepTime = train.inputDepTime(sc);

                                int rescheduleStationNum = train.inputStation(sc);
                                String rescheduleStation = train.getStationMap().get(rescheduleStationNum);

                                // Call train method to perform the rescheduling
                                train.rescheduleTrain(oldDepartureTime, newDepTime, rescheduleStation);
                                break;
                        }
                    }
                    break;
                
                case 6: // Delay train(s)
                    System.out.println("Option 6 selected: Delay train(s).");
                    System.out.print("How many schedules do you want to delay? ");
                    int delayCount = 1;
                    while (true) {
                        if (sc.hasNextInt()) {
                            delayCount = sc.nextInt();
                            if (delayCount > 0) break;
                            else System.out.print("Please enter a positive number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < delayCount; i++) {
                        System.out.println("Find the train using\n1. Train ID?\n2. Departure time?\nSelect the option: ");
                        int delayOption = 0;
                        validInput = false;
                        while (!validInput) {
                            if (sc.hasNextInt()) {
                                delayOption = sc.nextInt();
                                if (delayOption == 1 || delayOption == 2) {
                                    validInput = true;
                                } else {
                                    System.out.print("Invalid option! Please enter 1 or 2: ");
                                }
                            } else {
                                System.out.print("Invalid input! Please enter 1 or 2: ");
                                sc.next();
                            }
                        }
                        sc.nextLine(); // Consume the newline character

                        switch (delayOption) {
                            case 1:
                                System.out.println("You selected option 1: Train ID.");
                                System.out.print("Enter the train ID to delay: ");
                                String delayTrainID = sc.nextLine().toUpperCase();
                                System.out.print("Enter delay in minutes: ");
                                delayMinutes = 0;
                                while (true) {
                                    if (sc.hasNextInt()) {
                                        delayMinutes = sc.nextInt();
                                        if (delayMinutes >= 0) break;
                                        else System.out.print("Please enter a non-negative number: ");
                                    } else {
                                        System.out.print("Invalid input! Please enter a non-negative number: ");
                                        sc.next();
                                    }
                                }
                                sc.nextLine(); // consume newline
                                train.delayTrain(delayTrainID, delayMinutes);
                                break;
                            case 2:
                                System.out.print("Enter delay in minutes: ");
                                delayMinutes = 0;
                                while (true) {
                                    if (sc.hasNextInt()) {
                                        delayMinutes = sc.nextInt();
                                        if (delayMinutes >= 0) break;
                                        else System.out.print("Please enter a non-negative number: ");
                                    } else {
                                        System.out.print("Invalid input! Please enter a non-negative number: ");
                                        sc.next();
                                    }
                                }
                                System.out.println("You selected option 2: Departure time.");
                                System.out.print("Enter the departure time to delay (format: HHMM): ");
                                int oldDelayTime = train.inputDepTime(sc);
                                train.delayTrain(oldDelayTime, delayMinutes);
                                break;
                        }
                    }
                    break;
                case 7: // Cancel train(s)
                    System.out.println("Option 7 selected: Cancel train(s).");
                    System.out.print("How many schedules do you want to cancel? ");
                    int cancelCount = 1;
                    while (true) {
                        if (sc.hasNextInt()) {
                            cancelCount = sc.nextInt();
                            if (cancelCount > 0) break;
                            else System.out.print("Please enter a positive number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < cancelCount; i++) {
                        System.out.println("Find the train using\n1. Train ID?\n2. Departure time?\nSelect the option: ");
                        int cancelOption = 0;
                        validInput = false;
                        while (!validInput) {
                            if (sc.hasNextInt()) {
                                cancelOption = sc.nextInt();
                                if (cancelOption == 1 || cancelOption == 2) {
                                    validInput = true;
                                } else {
                                    System.out.print("Invalid option! Please enter 1 or 2: ");
                                }
                            } else {
                                System.out.print("Invalid input! Please enter 1 or 2: ");
                                sc.next();
                            }
                        }
                        sc.nextLine(); // Consume the newline character

                        switch (cancelOption) {
                            case 1:
                                System.out.println("You selected option 1: Train ID.");
                                System.out.print("Enter the train ID to cancel: ");
                                String cancelTrainID = sc.nextLine().toUpperCase();
                                train.cancelTrain(cancelTrainID);
                                break;
                            case 2:
                                System.out.println("You selected option 2: Departure time.");
                                System.out.print("Enter the departure time to cancel (format: HHMM): ");
                                int oldCancelTime = train.inputDepTime(sc);
                                train.cancelTrain(oldCancelTime);
                                break;
                        }
                    }
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
