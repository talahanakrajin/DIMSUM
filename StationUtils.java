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

    // Prompts user to select a station number (0 for "keep original station" is allowed)
    public static int stationSelection(Scanner sc, TreeMap<Integer, String> stationMap) {
        System.out.println("\nList of stations:");
        stationMap.forEach((key, value) -> {
            System.out.println(key + ". " + value);
        });
        System.out.print("Enter the station number to reschedule (Enter 0 to keep the original station): ");

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
}