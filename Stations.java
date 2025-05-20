import java.util.TreeMap;
import java.util.Scanner;

public class Stations {
    private TreeMap<Integer, String> stationMap;
    private TreeMap<String, Integer> forwardTravelTimeMap;
    private TreeMap<String, Integer> backwardTravelTimeMap;
    private Scanner sc = new Scanner(System.in);

    private final int terminusHaltTime = 3; // Time in minutes where the train halts at the terminus station (should be customizable)

    // Constructor
    public Stations(){
        stationMap = new TreeMap<>();
        forwardTravelTimeMap = new TreeMap<>();
        backwardTravelTimeMap = new TreeMap<>();
        initializeStations();
        initializeTravelTimes();
    }
    private void initializeStations(){
        stationMap.put(1, "Lebak Bulus");
        stationMap.put(2, "Fatmawati");
        stationMap.put(3, "Cipete Raya");
        stationMap.put(4, "Haji Nawi");
        stationMap.put(5, "Blok A");
        stationMap.put(6, "Blok M");
        stationMap.put(7, "ASEAN");
        stationMap.put(8, "Senayan");
        stationMap.put(9, "Istora");
        stationMap.put(10, "Bendungan Hilir");
        stationMap.put(11, "Setiabudi");
        stationMap.put(12, "Dukuh Atas");
        stationMap.put(13, "Bundaran HI");
    }
    // Values are in minutes
    private void initializeTravelTimes(){
        // Forward travel times
        forwardTravelTimeMap.put("1-2", 3);  // Lebak Bulus -> Fatmawati
        forwardTravelTimeMap.put("2-3", 3);  // Fatmawati -> Cipete Raya
        forwardTravelTimeMap.put("3-4", 2);  // Cipete Raya -> Haji Nawi
        forwardTravelTimeMap.put("4-5", 2);  // Haji Nawi -> Blok A
        forwardTravelTimeMap.put("5-6", 3);  // Blok A -> Blok M
        forwardTravelTimeMap.put("6-7", 2);  // Blok M -> ASEAN
        forwardTravelTimeMap.put("7-8", 2);  // ASEAN -> Senayan
        forwardTravelTimeMap.put("8-9", 2);  // Senayan -> Istora
        forwardTravelTimeMap.put("9-10", 3); // Istora -> Bendungan Hilir
        forwardTravelTimeMap.put("10-11", 2); // Bendungan Hilir -> Setiabudi
        forwardTravelTimeMap.put("11-12", 2); // Setiabudi -> Dukuh Atas
        forwardTravelTimeMap.put("12-13", 3 + terminusHaltTime); // Dukuh Atas -> Bundaran HI

        // Backward travel times
        backwardTravelTimeMap.put("13-12", 3); // Bundaran HI -> Dukuh Atas
        backwardTravelTimeMap.put("12-11", 2); // Dukuh Atas -> Setiabudi
        backwardTravelTimeMap.put("11-10", 2); // Setiabudi -> Bendungan Hilir
        backwardTravelTimeMap.put("10-9", 3); // Bendungan Hilir -> Istora
        backwardTravelTimeMap.put("9-8", 2); // Istora -> Senayan
        backwardTravelTimeMap.put("8-7", 2); // Senayan -> ASEAN
        backwardTravelTimeMap.put("7-6", 2); // ASEAN -> Blok M
        backwardTravelTimeMap.put("6-5", 3); // Blok M -> Blok A
        backwardTravelTimeMap.put("5-4", 2); // Blok A -> Haji Nawi
        backwardTravelTimeMap.put("4-3", 2); // Haji Nawi -> Cipete Raya
        backwardTravelTimeMap.put("3-2", 3); // Cipete Raya -> Fatmawati
        backwardTravelTimeMap.put("2-1", 3 + terminusHaltTime); // Fatmawati -> Lebak Bulus
    }

    public TreeMap<Integer, String> getStationMap() {
        return stationMap;
    }
    public TreeMap<String, Integer> getForwardTravelTimeMap() {
        return forwardTravelTimeMap;
    }
    public TreeMap<String, Integer> getBackwardTravelTimeMap() {
        return backwardTravelTimeMap;
    }
    
    public int checkStationNumber(int stationNumber) {
        boolean validInput = false;
        while (!validInput) {
            if (sc.hasNextInt()) {
                stationNumber = sc.nextInt();
                if (getStationMap().containsKey(stationNumber)) {
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
    public int stationSelection() {
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
