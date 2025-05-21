import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Commented out user interaction code for complexity analysis
        /*
        int choice;
        int stationNumber = 0;
        Scanner sc = new Scanner(System.in);
        String trainID = null;
        int departureTime = 0;
        int delayMinutes = 0;
        boolean validInput = false;
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
                /* For the users! 
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
                    train.printNextTrain();
                    break;

                /* For the manager! 
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
                        System.out.println("\nPlease enter the following details to add a new train schedule:\nWhich station do you want to add a train schedule to?\n\n1. Lebak Bulus (Start Terminus)\n2. Bundaran HI (End Terminus)\n3. Blok M (Middle Parking)");
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
                                System.out.println("You selected station: Blok M (Middle Parking)");
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
                                System.out.println("You selected option 2: Departure time.");
                                System.out.print("Enter the departure time to delay (format: HHMM): ");
                                int oldDelayTime = train.inputDepTime(sc);

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
                    System.out.println("Invalid choice. Please select a number between 1 and 9.");
            }
        } while (choice != 9); // while the user does not select 8 (exit) do the switch statements
        sc.close(); 
        */
        

        // Begin: Direct data input for complexity analysis
        Train train = new Train(null, 0);

        // Add schedules (n = number of schedules)
        int n = 100; 

        // Time and space complexity for addToScheduleMap (adding n schedules)
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        for (int i = 0; i < n; i++) {
            String trainID = String.format("TS%04d", i);
            int depTime = 500 + i;
            String station = train.getStationMap().get(1);
            train.addToScheduleMap(depTime, trainID, station);
        }
        long endTime = System.nanoTime();
        runtime.gc();
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        double elapsedMs = (endTime - startTime) / 1000000.0;
        double usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for adding " + n + " schedules: " + elapsedMs + " ms");
        System.out.println("Memory used by addToScheduleMap: " + usedMemMB + " MB\n");

        // Time and space complexity for printSchedule (full)
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.printSchedule(null);
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for printSchedule (all): " + elapsedMs + " ms");
        System.out.println("Memory used by printSchedule (all): " + usedMemMB + " MB\n");

        // Time and space complexity for printSchedule (filtered)
        String testStation = train.getStationMap().get(1);
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.printSchedule(testStation);
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for printSchedule (filtered): " + elapsedMs + " ms");
        System.out.println("Memory used by printSchedule (filtered): " + usedMemMB + " MB\n");

        // Time and space complexity for findNextTrain
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.printNextTrain();
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for findNextTrain: " + elapsedMs + " ms");
        System.out.println("Memory used by findNextTrain: " + usedMemMB + " MB\n");

        // Time and space complexity for rescheduleTrain by TrainID
        String rescheduleID = "TS0001";
        int newDepTime = 1120;
        String newStation = train.getStationMap().get(2);
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.rescheduleTrain(rescheduleID, newDepTime, newStation);
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for rescheduleTrain by ID: " + elapsedMs + " ms");
        System.out.println("Memory used by rescheduleTrain: " + usedMemMB + " MB\n");

        // Time and space complexity for delayTrain by TrainID
        String delayID = "TS0002";
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.delayTrain(delayID, 10);
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for delayTrain by ID: " + elapsedMs + " ms");
        System.out.println("Memory used by delayTrain: " + usedMemMB + " MB\n");

        // Time and space complexity for cancelTrain by TrainID
        String cancelID = "TS0003";
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.cancelTrain(cancelID);
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for cancelTrain by ID: " + elapsedMs + " ms");
        System.out.println("Memory used by cancelTrain: " + usedMemMB + " MB\n");

        // Time and space complexity for simulateTrainRunning
        int closingTime = 2300;
        runtime.gc();
        beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        train.simulateTrainRunning(closingTime);
        endTime = System.nanoTime();
        runtime.gc();
        afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        elapsedMs = (endTime - startTime) / 1000000.0;
        usedMemMB = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.println("Time for simulateTrainRunning: " + elapsedMs + " ms");
        System.out.println("Memory used by simulateTrainRunning: " + usedMemMB + " MB\n");
    }
}