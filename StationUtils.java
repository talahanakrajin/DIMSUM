import java.util.Scanner;
import java.util.TreeMap;

public final class StationUtils {
    private StationUtils() {} // Prevent instantiation

    // Make these static so they can be used from anywhere
    public static int checkStationNumber(Scanner sc, TreeMap<Integer, String> stationMap) {
        int stationNumber = -1;
        boolean validInput = false;
        while (!validInput) {
            if (sc.hasNextInt()) {
                stationNumber = sc.nextInt();
                if (stationMap.containsKey(stationNumber)) {
                    validInput = true;
                } else {
                    System.out.print("Invalid station number! Please enter a number between 1 - 13: ");
                }
            } else {
                System.out.print("Invalid input! Please enter a number: ");
                sc.next(); // Consume invalid input
            }
        }
        sc.nextLine(); // Consume the newline character after number input
        return stationNumber;
    }

    /**
     * Prints the list of stations with their numbers.
     */
    public static void printStationList(TreeMap<Integer, String> stationMap) {
        System.out.println("\nList of stations:");
        stationMap.forEach((key, value) -> {
            System.out.println(key + ". " + value);
        });
    }

    /**
     * Prompts user to select a station and returns the station number.
     */
    public static int stationSelection(Scanner sc, TreeMap<Integer, String> stationMap) {
        int stationNum = -1;
        boolean validInput = false;
        while (!validInput) {
            if (sc.hasNextInt()) {
                stationNum = sc.nextInt();
                if (stationNum == 0 || stationMap.containsKey(stationNum)) {
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

    /**
     * Gets the station name from the station number.
     */
    public static String getStationName(int stationNum, TreeMap<Integer, String> stationMap) {
        return stationMap.get(stationNum);
    }

    /**
     * Prints the schedule header for a specific station.
     */
    public static void printStationScheduleHeader(String stationName) {
        System.out.println("\nSchedule for " + stationName + ":");
    }

    /**
     * Determines if a train at a given station should be northbound.
     * @param stationName The name of the station
     * @return true if the train should be northbound, false if southbound
     */
    public static boolean checkInitialDirection(String stationName) {
        Stations stations = new Stations();
        int stationNum = -1;
        for (var entry : stations.getStationMap().entrySet()) {
            if (entry.getValue().equals(stationName)) {
                stationNum = entry.getKey();
                break;
            }
        }
        return stationNum < 13; // If not at Bundaran HI (13), train is northbound
    }
}