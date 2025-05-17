import java.util.Scanner;
import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;

    private boolean isAvailable;
    private boolean isOnTime; 
    
    TreeMap<Integer, String> scheduleMap = new TreeMap<>(); // departure time -> "TrainID,StationName"

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
        scheduleMap.put(departureTime, trainID + "," + stationName);
    }

    // Print all or filtered by station
    public void printSchedule(String selectedStation) {
        boolean found = false;
        for (var entry : scheduleMap.entrySet()) {
            int depTime = entry.getKey();
            String[] parts = entry.getValue().split(",", 2);
            String trainID = parts[0];
            String stationName = parts.length > 1 ? parts[1] : "";
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
}
