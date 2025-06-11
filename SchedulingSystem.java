/**
 * Main scheduling system for trains.
 * Demonstrates use of Java Collections, exception handling, custom classes, and polymorphism.
 */
public class SchedulingSystem {
    private final Stations stationsData = new Stations();
    private final String managerPassword = "eatdimsumeveryday";

    public boolean checkManagerPassword(String password) {
        return managerPassword.equals(password);
    }

    // Delegates to MRT for data retrieval and processing
    public Schedulable getTrainById(String id) {
        return MRTManager.getTrainById(id);
    }

    /* ALL METHODS BELOW ARE PASS THROUGH METHODS */
    // Adds a train schedule to the system
    public void addTrain(Schedulable schedule) {
        MRTManager.addTrain(schedule);
    }
    // Prints all train schedules
    public void printAllSchedules() {
        MRTManager.printAllSchedules();
    }
    // Prints the train schedule for a specific station
    public void printStationSchedule(String station) {
        MRTManager.printStationSchedule(station);
    }
    // Gets the next departing train in the system
    public void getNextTrain() {
        MRTManager.getNextTrain();
    }
    // Gets the next departing train at a specific station
    public void getNextTrain(String stationName) {
        MRTManager.getNextTrain(stationName);
    }
    // Delays a train 
    public boolean delayTrain(String trainID, int departureTime, int delayMinutes, String reason) {
        return MRTManager.delayTrain(trainID, departureTime, delayMinutes, reason);
    }
    // Prints only delayed trains
    public void printDelayedTrains() {
        MRTManager.printDelayedTrains();
    }
    // Reschedules a train to a new departure time and station (optional)
    public boolean rescheduleTrain(String trainID, int oldDepartureTime, int newDepartureTime, String newStation) {
        return MRTManager.rescheduleTrain(trainID, oldDepartureTime, newDepartureTime, newStation);
    }
    // Cancel a train schedule
    public boolean cancelTrain(String trainID, int departureTime) {
        return MRTManager.cancelTrain(trainID, departureTime);
    }
    public void simulateTrainsRunning(int closingTime) {
        MRTManager.simulateTrainsRunning(closingTime);
    }

    /* PASSENGER & MANAGER MENU LOGIC */
    // Handles the passenger menu and all user input/validation for passenger actions
    public void passengerMenu(java.util.Scanner sc) {
        boolean running = true;
        while (running) {
            System.out.println("\nPassenger Menu:");
            System.out.println("1. View all schedules");
            System.out.println("2. View schedules for a station");
            System.out.println("3. Find next departing train at a station");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    printAllSchedules();
                    break;
                case "2":
                    printStationScheduleMenu(sc, stationsData.getStationMap());
                    break;
                case "3":
                    getNextTrainAtStation(sc, stationsData.getStationMap());
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Handles the manager menu and all user input/validation for manager actions
    public void managerMenu(java.util.Scanner sc) {
        boolean running = true;
        while (running) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Add train");
            System.out.println("2. Reschedule train");
            System.out.println("3. Delay train");
            System.out.println("4. Cancel train");
            System.out.println("5. View all schedules");
            System.out.println("6. View schedules for a station");
            System.out.println("7. Find next departing train");
            System.out.println("8. View delayed trains by priority");
            System.out.println("9. Simulate trains running");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addTrainMenu(sc);
                    break;
                case "2":
                    rescheduleTrainMenu(sc, stationsData);
                    break;
                case "3":
                    delayTrainMenu(sc);
                    break;
                case "4":
                    cancelTrainMenu(sc);
                    break;
                case "5":
                    printAllSchedules();
                    break;
                case "6":
                    printStationScheduleMenu(sc, stationsData.getStationMap());
                    break;
                case "7":
                    getNextTrainMenu(sc);
                    break;
                case "8":
                    printDelayedTrains();
                    break;
                case "9":
                    simulateTrainsRunningMenu(sc);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /* FUNCTIONS MENU LOGIC (For input validation and stuff) */
    // Handles the add train menu for the manager, including manual and automatic modes.
    public void addTrainMenu(java.util.Scanner sc) {
        System.out.println("Add Train Schedule Mode:");
        System.out.println("1. Add schedules manually");
        System.out.println("2. Add schedules automatically (batch)");
        System.out.print("Choose mode (1 or 2): ");
        int addMode = 1;
        boolean validInput = false;
        while (!validInput) {
            if (sc.hasNextInt()) {
                addMode = sc.nextInt();
                if (addMode == 1 || addMode == 2) {
                    validInput = true;
                } else {
                    System.out.print("Invalid input! Please enter 1 or 2: ");
                }
            } else {
                System.out.print("Invalid input! Please enter 1 or 2: ");
                sc.next();
            }
        }
        sc.nextLine(); // consume newline

        if (addMode == 1) {
            manualAddTrainMenu(sc);
        } else {
            autoAddTrainMenu(sc);
        }
    }

    private void manualAddTrainMenu(java.util.Scanner sc) {
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
            boolean validInput = false;
            int stationNumber = 0;
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
                case 1 -> stationName = "Lebak Bulus";
                case 2 -> stationName = "Bundaran HI";
                case 3 -> stationName = "Blok M";
            }
            sc.nextLine(); // Consume the newline character

            // Train ID
            String id = TrainUtils.promptValidTrainID(sc);
            // Departure time
            int depTime = TimeUtils.promptValidTime(sc);

            // Delegate to MRT for business logic
            TrainUtils.addTrain(this, id, depTime, stationName);
            System.out.println("Train schedule added successfully!");
        }
    }

    private void autoAddTrainMenu(java.util.Scanner sc) {
        System.out.println("\nWhich station do you want to add train schedules to automatically?");
        System.out.println("1. Lebak Bulus (Start Terminus)\n2. Bundaran HI (End Terminus)\n3. Blok M (Middle Parking)");
        int autoStationNum = 0;
        String autoStationName = null;
        boolean validInput = false;
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
        autoStationName = switch (autoStationNum) {
            case 1 -> "Lebak Bulus";
            case 2 -> "Bundaran HI";
            case 3 -> "Blok M";
            default -> null;
        };
        sc.nextLine(); // consume newline

        System.out.print("Enter the START TIME: ");
        int startTime = TimeUtils.promptValidTime(sc);
        System.out.print("Enter the END TIME: ");
        int endTime = TimeUtils.promptValidTime(sc);
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

        // Delegate to MRT for business logic
        TrainUtils.autoAddTrain(this, autoStationName, startTime, endTime, headway);
        System.out.println("Automatic schedule addition completed.");
    }

    private void getNextTrainMenu(java.util.Scanner sc) {
        StationUtils.printStationList(stationsData.getStationMap());
        System.out.print("Choose Station (Enter '0' for finding the next departing train in the system): ");
        int stationNum = StationUtils.stationSelection(sc, stationsData.getStationMap());
        if (stationNum == 0) {
            getNextTrain();
        } else {
            String stationName = StationUtils.getStationName(stationNum, stationsData.getStationMap());
            getNextTrain(stationName);
        }
    }

    private void rescheduleTrainMenu(java.util.Scanner sc, Stations stationData) {
        String trainID = TrainUtils.promptValidTrainID(sc);
        System.out.print("Enter Current Departure Time: ");
        int oldDep = TimeUtils.promptValidTime(sc);
        System.out.print("Enter New Departure Time: ");
        int newDep = TimeUtils.promptValidTime(sc);
        StationUtils.printStationList(stationData.getStationMap());
        System.out.print("Enter the station number (Enter 0 to keep the original station): ");
        int newStationNum = StationUtils.stationSelection(sc, stationData.getStationMap());
        String newStation;
        if (newStationNum == 0) {
            MRT train = (MRT) MRTManager.getTrainByIdAndTime(trainID, oldDep);
            if (train != null) {
                newStation = train.getCurrentStation();
            } else {
                newStation = null;
            }
        } else {
            newStation = StationUtils.getStationName(newStationNum, stationData.getStationMap());
        }
        if (rescheduleTrain(trainID, oldDep, newDep, newStation)) {
            System.out.println("Train rescheduled.");
        } else {
            System.out.println("Train not found at the specified time.");
        }
    }

    private void delayTrainMenu(java.util.Scanner sc) {
        String delayID = TrainUtils.promptValidTrainID(sc);
        System.out.print("Enter Current Departure Time: ");
        int depTime = TimeUtils.promptValidTime(sc);
        int delay = TimeUtils.promptValidDelayMinutes(sc);
        System.out.print("Enter reason for delay: ");
        String reason = sc.nextLine();
        if (delayTrain(delayID, depTime, delay, reason)) {
            System.out.println("Train delayed.");
        } else {
            System.out.println("Train not found at the specified time.");
        }
    }

    private void cancelTrainMenu(java.util.Scanner sc) {
        String cancelID = TrainUtils.promptValidTrainID(sc);
        System.out.print("Enter Departure Time: ");
        int depTime = TimeUtils.promptValidTime(sc);
        if (cancelTrain(cancelID, depTime)) {
            System.out.println("Train cancelled.");
        } else {
            System.out.println("Train not found at the specified time.");
        }
    }
    private void simulateTrainsRunningMenu(java.util.Scanner sc) {
        System.out.print("Enter Closing Time (HHMM): ");
        int closingTime = TimeUtils.promptClosingTime(sc);
        simulateTrainsRunning(closingTime);
        System.out.println("Simulation completed.");
    }

    private void getNextTrainAtStation(java.util.Scanner sc, java.util.TreeMap<Integer, String> stationMap) {
        StationUtils.printStationList(stationMap);
        System.out.print("Choose Station: ");
        int stationNum = StationUtils.stationSelection(sc, stationMap);
        String stationName = StationUtils.getStationName(stationNum, stationMap);
        getNextTrain(stationName);
    }

    private void printStationScheduleMenu(java.util.Scanner sc, java.util.TreeMap<Integer, String> stationMap) {
        StationUtils.printStationList(stationMap);
        System.out.print("Choose Station: ");
        int stationNum = StationUtils.stationSelection(sc, stationMap);
        String stationName = StationUtils.getStationName(stationNum, stationMap);
        printStationSchedule(stationName);
    }
}