import java.util.TreeMap;
import java.util.Scanner;

public class Stations {
    private TreeMap<Integer, String> stationMap;
    private TreeMap<String, Integer> travelTimeMap;
    private Scanner sc = new Scanner(System.in);

    // Constructor
    public Stations(){
        stationMap = new TreeMap<>();
        travelTimeMap = new TreeMap<>();
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
        travelTimeMap.put("1-2", 3);  // Lebak Bulus -> Fatmawati
        travelTimeMap.put("2-3", 3);  // Fatmawati -> Cipete Raya
        travelTimeMap.put("3-4", 2);  // Cipete Raya -> Haji Nawi
        travelTimeMap.put("4-5", 2);  // Haji Nawi -> Blok A
        travelTimeMap.put("5-6", 3);  // Blok A -> Blok M
        travelTimeMap.put("6-7", 2);  // Blok M -> ASEAN
        travelTimeMap.put("7-8", 2);  // ASEAN -> Senayan
        travelTimeMap.put("8-9", 2);  // Senayan -> Istora
        travelTimeMap.put("9-10", 3); // Istora -> Bendungan Hilir
        travelTimeMap.put("10-11", 2); // Bendungan Hilir -> Setiabudi
        travelTimeMap.put("11-12", 2); // Setiabudi -> Dukuh Atas
        travelTimeMap.put("12-13", 9); // Dukuh Atas -> Bundaran HI
    }

    public TreeMap<Integer, String> getStationMap() {
        return stationMap;
    }
    public TreeMap<String, Integer> getTravelTimeMap() {
        return travelTimeMap;
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
