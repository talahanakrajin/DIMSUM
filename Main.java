import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Commented out user interaction code for complexity analysis
        int choice;
        int stationNumber = 0;
        Scanner sc = new Scanner(System.in);
        String trainID = null;
        int departureTime = 0;
        int delayMinutes = 0;
        boolean validInput = false;
        Train train = new Train(trainID, departureTime);
        System.out.println("\nWelcome to DIMSUM 'Digital Interactive MRT Schedule Update Manager'");
        do {
            System.out.println("\nPlease select an option:");
            System.out.println("1. View full schedule");
            System.out.println("2. View each station's schedule");
            System.out.println("3. Find next train departing");
            System.out.println("4. Add train schedule(s)");
            System.out.println("5. Reschedule train(s)");
            System.out.println("6. Delay train(s)");
            System.out.println("7. Cancel train(s)");
            System.out.println("8. Simulate trains running");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) { // Handles invalid inputs
                System.out.println("Wrong Input! Please enter a number between 1 and 9.");
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
                        System.out.println(key + ". " + value);
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
                    train.printNextTrain();
                    break;

                /* For the manager! */
                case 4: // Add train schedule(s)
                    System.out.println("\nOption 4 selected: Add train schedule(s).\n");
                    System.out.print("Do you want to add schedules manually or automatically? (1 = Manual, 2 = Automatic): ");
                    int addMode = 1;
                    while (true) {
                        if (sc.hasNextInt()) {
                            addMode = sc.nextInt();
                            if (addMode == 1 || addMode == 2) break;
                            else System.out.print("Please enter 1 for Manual or 2 for Automatic: ");
                        } else {
                            System.out.print("Invalid input! Please enter 1 or 2: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    if (addMode == 1) {
                        // Manual adding (existing code)
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
                            System.out.println("\nPlease enter the following details to add a new train schedule:\nWhich station do you want to add a train schedule to?\n\n1. Lebak Bulus (Start Terminus)\n2. Bundaran HI (End Terminus)\n3. Blok M (Middle Parking)");
                            validInput = false;
                            String stationName = null;
                            while (!validInput) {
                                if (sc.hasNextInt()) {
                                    stationNumber = sc.nextInt();
                                    if (stationNumber < 1 || stationNumber > 3) {
                                        System.out.print("Invalid station Number! Please enter a Number between 1 - 3: ");
                                    } else {
                                        validInput = true;
                                    }
                                } else {
                                    System.out.print("Invalid input! Please enter a Number between 1 - 3: ");
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
                                    System.out.println("You selected station: Blok M (Middle Parking)");
                                    stationName = "Blok M";
                                    break;
                            }
                            sc.nextLine(); // Consume the newline character
                            System.out.println("Please enter the following details to add a new train schedule:\n");

                            // Train ID
                            train.inputTrainID(sc);
                            // System.out.println("DEBUG PRINT Train ID: " + train.getTrainID());
                            // Departure time
                            int depTime = train.inputDepTime(sc);
                            // System.out.println("DEBUG PRINT departure time: " + depTime);

                            train.addToScheduleMap(depTime, train.getTrainID(), stationName, false);
                            System.out.println("Train schedule added successfully!");
                        }
                    } else {
                        // Automatic adding
                        System.out.println("\nWhich station do you want to add train schedules to automatically?");
                        System.out.println("1. Lebak Bulus (Start Terminus)\n2. Bundaran HI (End Terminus)\n3. Blok M (Middle Parking)");
                        int autoStationNum = 0;
                        String autoStationName = null;
                        validInput = false;
                        while (!validInput) {
                            if (sc.hasNextInt()) {
                                autoStationNum = sc.nextInt();
                                if (autoStationNum >= 1 && autoStationNum <= 3) {
                                    validInput = true;
                                } else {
                                    System.out.print("Invalid station Number! Please enter a Number between 1 - 3: ");
                                }
                            } else {
                                System.out.print("Invalid input! Please enter a Number between 1 - 3: ");
                                sc.next();
                            }
                        }
                        switch (autoStationNum) {
                            case 1:
                                autoStationName = "Lebak Bulus";
                                break;
                            case 2:
                                autoStationName = "Bundaran HI";
                                break;
                            case 3:
                                autoStationName = "Blok M";
                                break;
                        }
                        sc.nextLine(); // consume newline

                        System.out.print("Enter the START TIME: ");
                        int startTime = train.inputDepTime(sc);
                        System.out.print("Enter the END TIME: ");
                        int endTime = train.inputDepTime(sc);
                        System.out.print("Enter the headway in minutes: ");
                        int headway = 0;
                        while (true) {
                            if (sc.hasNextInt()) {
                                headway = sc.nextInt();
                                if (headway > 0) break;
                                else System.out.print("Please enter a positive headway in minutes: ");
                            } else {
                                System.out.print("Invalid input! Please enter a positive headway in minutes: ");
                                sc.next();
                            }
                        }
                        sc.nextLine(); // consume newline

                        // Use the new function for automatic schedule addition
                        train.addSchedulesAutomatically(autoStationName, startTime, endTime, headway);
                        System.out.println("Automatic schedule addition completed.");
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
                            else System.out.print("Please enter a positive Number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive Number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < rescheduleCount; i++) {
                        System.out.print("Enter the train ID to reschedule: ");
                        String rescheduleTrainID = sc.nextLine().toUpperCase();
                        System.out.print("Enter the current departure time (format: HHMM): ");
                        int oldDepartureTime = train.inputDepTime(sc);
                        System.out.print("Enter the new departure time (format: HHMM): ");
                        int newDepTime = train.inputDepTime(sc);
                        int rescheduleStationNum = train.inputStation(sc);
                        String rescheduleStation = train.getStationMap().get(rescheduleStationNum);
                        train.rescheduleTrain(rescheduleTrainID, oldDepartureTime, newDepTime, rescheduleStation);
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
                            else System.out.print("Please enter a positive Number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive Number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < delayCount; i++) {
                        System.out.print("Enter the train ID to delay: ");
                        String delayTrainID = sc.nextLine().toUpperCase();
                        System.out.print("Enter the current departure time (format: HHMM): ");
                        int oldDelayTime = train.inputDepTime(sc);
                        System.out.print("Enter delay in minutes: ");
                        delayMinutes = 0;
                        while (true) {
                            if (sc.hasNextInt()) {
                                delayMinutes = sc.nextInt();
                                if (delayMinutes >= 0) break;
                                else System.out.print("Please enter a non-negative Number: ");
                            } else {
                                System.out.print("Invalid input! Please enter a non-negative Number: ");
                                sc.next();
                            }
                        }
                        sc.nextLine(); // consume newline
                        train.delayTrain(delayTrainID, oldDelayTime, delayMinutes);
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
                            else System.out.print("Please enter a positive Number: ");
                        } else {
                            System.out.print("Invalid input! Please enter a positive Number: ");
                            sc.next();
                        }
                    }
                    sc.nextLine(); // consume newline

                    for (int i = 0; i < cancelCount; i++) {
                        System.out.print("Enter the train ID to cancel: ");
                        String cancelTrainID = sc.nextLine().toUpperCase();
                        System.out.print("Enter the departure time to cancel (format: HHMM): ");
                        int oldCancelTime = train.inputDepTime(sc);
                        train.cancelTrain(cancelTrainID, oldCancelTime);
                    }
                    break;
                case 8: // Simulate train running (this creates the whole schedule for the day)
                    System.out.println("Option 8 selected: Simulate trains running.");
                    System.out.print("Enter the closing time (format: HHMM): ");
                    int closingTime = 0;
                    while (true) {
                        if (sc.hasNextInt()) {
                            closingTime = sc.nextInt();
                            if (closingTime >= 0 && closingTime <= 2359) {
                                break;
                            } else {
                                System.out.print("Please enter a valid time in HHMM format: ");
                            }
                        } else {
                            System.out.print("Invalid input! Please enter a valid time in HHMM format: ");
                            sc.next();
                        }
                    }
                    train.simulateTrainRunning(closingTime);
                    break;
                case 9: // Exit
                    System.out.println("Exiting the Train Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a Number between 1 and 9.");
            }
        } while (choice != 9); // while the user does not select 8 (exit) do the switch statements
        sc.close(); 
    }
}