/**
 * Main scheduling system for trains.
 * Demonstrates use of Java Collections, exception handling, custom classes, and polymorphism.
 */
public class SchedulingSystem {
    private final String managerPassword = "eatdimsumeveryday";

    public boolean checkManagerPassword(String password) {
        return managerPassword.equals(password);
    }

    // Delegates to MRT for data retrieval and processing
    public Schedulable getTrainById(String id) {
        // Delegates to MRT for data retrieval
        return MRT.getTrainById(id);
    }

    /* ALL METHODS BELOW ARE PASS THROUGH METHODS */
    // Adds a train schedule to the system
    public void addTrain(Schedulable schedule) {
        MRT.addTrain(schedule);
    }
    // Prints all train schedules
    public void printAllSchedules() {
        MRT.printAllSchedules();
    }
    // Prints the train schedule for a specific station
    public void printStationSchedule(String stationName) {
        MRT.printStationSchedule(stationName);
    }
    // Gets the next departing train in the system
    public void getNextTrain() {
        Schedulable nextTrain = MRT.getNextTrain();
        if (nextTrain != null) {
            System.out.println("Next train in the system: " + nextTrain);
        } else {
            System.out.println("No trains found in the system.");
        }
    }
    // Gets the next departing train at a specific station
    public void getNextTrain(String stationName) {
        Schedulable nextTrain = MRT.getNextTrain(stationName);
        if (nextTrain != null) {
            System.out.println("Next train at " + stationName + ": " + nextTrain);
        } else {
            System.out.println("No trains found for station: " + stationName);
        }
    }
    // Delays a train 
    public void delayTrain(String trainID, int delayMinutes) {
        MRT.delayTrain(trainID, delayMinutes);
    }
    // Prints only delayed trains sorted by priority
    public void printDelayedTrainsByPriority() {
        MRT.printDelayedTrainsByPriority();
    }
    // Reschedules a train to a new departure time and station (optional)
    public void rescheduleTrain(String trainID, int newDepartureTime, String newStation) {
        MRT.rescheduleTrain(trainID, newDepartureTime, newStation);
    }
    // Cancel a train schedule
    public void cancelTrain(String trainID) {
        MRT.cancelTrain(trainID);
    }

    /* PASSENGER & MANAGER MENU LOGIC */
    // Handles the passenger menu and all user input/validation for passenger actions
    public void passengerMenu(java.util.Scanner sc) {
        Stations stationsData = new Stations();
        boolean running = true;
        while (running) {
            System.out.println("\nPassenger Menu:");
            System.out.println("1. View all schedules");
            System.out.println("2. View schedules for a station");
            System.out.println("3. Find next departing train at a station");
            System.out.println("4. View delayed trains by priority");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    MRT.printAllSchedules();
                    break;
                case "2":
                    int stationNum = StationUtils.stationSelection(sc, stationsData.getStationMap());
                    String station = stationsData.getStationMap().get(stationNum);
                    MRT.printStationSchedule(station);
                    break;
                case "3":
                    int stNum = StationUtils.stationSelection(sc, stationsData.getStationMap());
                    String st = stationsData.getStationMap().get(stNum);
                    getNextTrain(st);
                    break;
                case "4":
                    MRT.printDelayedTrainsByPriority();
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
        Stations staticStations = new Stations();
        boolean running = true;
        while (running) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Add train");
            System.out.println("2. Reschedule train");
            System.out.println("3. Delay train");
            System.out.println("4. Cancel train");
            System.out.println("5. View all schedules");
            System.out.println("6. View schedules for a station");
            System.out.println("7. Find next departing train (global)");
            System.out.println("8. Find next departing train at a station");
            System.out.println("9. View delayed trains by priority");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    addTrainMenu(sc);
                    break;
                case "2":
                    rescheduleTrainMenu(sc, staticStations);
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
                    int stationNum = StationUtils.checkStationNumber(sc, staticStations.getStationMap());
                    String st = staticStations.getStationMap().get(stationNum);
                    printStationSchedule(st);
                    break;
                case "7":
                    getNextTrainMenu(sc);
                    break;
                case "8":
                    printDelayedTrainsByPriority();
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
            int depTime = TimeUtils.promptValidDepartureTime(sc);

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
        int startTime = TimeUtils.promptValidDepartureTime(sc);
        System.out.print("Enter the END TIME: ");
        int endTime = TimeUtils.promptValidDepartureTime(sc);
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
        System.out.print("Enter Station Name (Enter '0' for finding the next departing train in the system): ");
        String stationName = sc.nextLine();
        if (stationName.equals("0")) {
            getNextTrain();
        } else {
            getNextTrain(stationName);
        }
    }

    private void rescheduleTrainMenu(java.util.Scanner sc, Stations staticStations) {
        System.out.print("Enter Train ID: ");
        String trainID = TrainUtils.promptValidTrainID(sc);
        int newDep = TimeUtils.promptValidDepartureTime(sc);
        int newStationNum = StationUtils.stationSelection(sc, staticStations.getStationMap());
        String newStation;
        if (newStationNum == 0) {
            MRT train = (MRT) getTrainById(trainID);
            if (train != null) {
                newStation = train.getCurrentStation();
            } else {
                newStation = null;
            }
        } else {
            newStation = staticStations.getStationMap().get(newStationNum);
        }
        MRT.rescheduleTrain(trainID, newDep, newStation);
        System.out.println("Train rescheduled.");
    }

    private void delayTrainMenu(java.util.Scanner sc) {
        System.out.print("Enter Train ID: ");
        String delayID = TrainUtils.promptValidTrainID(sc);
        int delay = TimeUtils.promptValidDelayMinutes(sc);
        MRT.delayTrain(delayID, delay);
        System.out.println("Train delayed.");
    }

    private void cancelTrainMenu(java.util.Scanner sc) {
        System.out.print("Enter Train ID: ");
        String cancelID = sc.nextLine();
        MRT.cancelTrain(cancelID);
        System.out.println("Train cancelled.");
    }
}