import java.util.Scanner;
import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;

    private boolean isAvailable;
    private boolean isOnTime; 
    
    TreeMap<Integer, String> scheduleMap = new TreeMap<>(); // departure time: "TrainID,StationName"

    // Constructor
    public Train(String trainID, int departureTime) {
        this.trainID = trainID;
        this.departureTime = departureTime;
        this.isAvailable = true; 
        this.isOnTime = true; 
    }

    public String getTrainID() {
        return trainID;
    }
    public int getDepartureTime() {
        return departureTime;
    }
    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }
    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    // IS AVAILABLE
    public void checkTrainAvailability() {
        if (isAvailable) {
            System.out.println(trainID + " is available.");
        } else {
            System.out.println(trainID + " is out of service.");
        }
    }
    public void setTrainAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // IS ON TIME
    public void checkTrainOnTime() {
        if (isOnTime) {
            System.out.println(trainID + " is on time.");
        } else {
            System.out.println(trainID + " is delayed.");
        }
    }

    public TreeMap<Integer, String> getScheduleMap() {
        return scheduleMap;
    }

    public void addToScheduleMap(int departureTime, String trainID, String stationName) {
        scheduleMap.put(departureTime, trainID + "," + stationName); // departure time as the key, "TrainID,StationName" as the value
    }

    // Print all or filtered by station
    public void printSchedule(String selectedStation) {
        boolean found = false;
        for (var entry : scheduleMap.entrySet()) {
            int depTime = entry.getKey();
            String[] parts = entry.getValue().split(",", 2); // List of parts: [TrainID, StationName]
            String trainID = parts[0]; // Train ID
            String stationName = parts.length > 1 ? parts[1] : ""; // Station name
            if (selectedStation == null || stationName.equalsIgnoreCase(selectedStation)) {
                String depTimeStr = String.format("%04d", depTime);
                String hours = depTimeStr.substring(0, 2);
                String minutes = depTimeStr.substring(2, 4);
                System.out.printf("Train ID: %s | Departure Time: %s:%s | Station: %s%n", trainID, hours, minutes, stationName);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No trains found for the specified criteria.");
        }
    }

    // Input and validate train ID
    public void inputTrainID(Scanner sc) {
        String inputTrainID;
        while (true) {
            System.out.print("Enter the train ID with format: TSxxxx (Note that 'ts' will be converted to uppercase, and x = non-negative integer): ");
            inputTrainID = sc.nextLine().toUpperCase();
            if (inputTrainID.matches("TS[0-9]{4}")) {
                setTrainID(inputTrainID);
                break;
            } else {
                System.out.println("Invalid train ID! Enter with the format TSxxxx. (x = non-negative integer)");
            }
        }
    }

    // Input and validate departure time
    public void inputDepartureTime(Scanner sc) {
        int inputDepartureTime;
        while (true) {
            System.out.print("Enter the departure time (format: HHMM): ");
            if (sc.hasNextInt()) {
                inputDepartureTime = sc.nextInt();
                if (inputDepartureTime >= 0 && inputDepartureTime <= 2359 && inputDepartureTime % 100 < 60) {
                    setDepartureTime(inputDepartureTime);
                    sc.nextLine(); // consume newline
                    break;
                } else {
                    System.out.println("Invalid departure time! Enter between 0000-2359 and minutes must be 00-59.");
                }
            } else {
                System.out.println("Invalid input! Please enter a 4-digit number (HHMM).");
                sc.next(); // consume invalid input
            }
        }
    }

    // Input and validate departure time (returns the valid int)
    public int inputDepTime(Scanner sc) {
        int inputDepartureTime;
        while (true) {
            System.out.print("Enter the departure time (format: HHMM): ");
            if (sc.hasNextInt()) {
                inputDepartureTime = sc.nextInt();
                if (inputDepartureTime >= 0 && inputDepartureTime <= 2359 && inputDepartureTime % 100 < 60) {
                    sc.nextLine(); // consume newline
                    return inputDepartureTime;
                } else {
                    System.out.println("Invalid departure time! Enter between 0000-2359 and minutes must be 00-59.");
                }
            } else {
                System.out.println("Invalid input! Please enter a 4-digit number (HHMM).");
                sc.next(); // consume invalid input
            }
        }
    }

    /* Method overloading for rescheduling train */
    public void rescheduleTrain(int oldDepTime, int newDepTime, String newStation) {
        if (scheduleMap.containsKey(oldDepTime)) {
            String trainID_and_station = scheduleMap.get(oldDepTime);
            // Remove the old departure time, and replaces the station name
            scheduleMap.remove(oldDepTime);

            if (newStation == null) {
                System.out.println("No new station provided. Keeping the old station.");
            } else {
                trainID_and_station = trainID_and_station.split(",")[0] + "," + newStation; // Update station name
            }
            scheduleMap.put(newDepTime, trainID_and_station);
            System.out.println("Train rescheduled from " + oldDepTime + " to " + newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }
    public void rescheduleTrain(String trainID, int newDepTime, String newStation) {
        for (var entry : scheduleMap.entrySet()) {
            if (entry.getValue().startsWith(trainID)) {
                int oldDepTime = entry.getKey();
                scheduleMap.remove(oldDepTime);

                if (newStation == null) {
                    System.out.println("\nNo new station provided. Keeping the old station.");
                    System.out.print("Train Successfully rescheduled!\n");
                } else {
                    entry.setValue(trainID + "," + newStation); // Update station name
                }
                scheduleMap.put(newDepTime, entry.getValue());
                return;
            }
        }
        System.out.println("No train found with the specified train ID.");
    }

    // Prompts user to select a station number (0 for "keep original station" is allowed)
    public int inputStation(Scanner sc) {
        getStationMap().forEach((key, value) -> {
            System.out.println(key + ". " + value + "\n");
        });
        System.out.print("Enter the station number to reschedule (Enter 0 to keep the original station): ");

        int stationNum = -1;
        boolean validInput = false;
        while (!validInput) {
            if (sc.hasNextInt()) {
                stationNum = sc.nextInt();
                if (stationNum == 0 || getStationMap().containsKey(stationNum)) {
                    validInput = true;
                } else {
                    System.out.print("Invalid station number! Please enter a number between 0 - 13: ");
                }
            } else {
                System.out.print("Invalid input! Please enter a number between 0 - 13: ");
                sc.next();
            }
        }
        sc.nextLine(); // Consume the newline character
        return stationNum;
    }
}
