import java.util.TreeMap;

/**
 * Holds static station and travel time data.
 * Demonstrates use of Java Collections, instance variables, and defensive copying for immutability.
 */
public class Stations {
    private final TreeMap<Integer, String> stationMap;
    private final TreeMap<String, Integer> forwardTravelTimeMap;
    private final TreeMap<String, Integer> backwardTravelTimeMap;

    private final int terminusHaltTime = 3; // Time in minutes where the train halts at the terminus station (should be customizable)

    /** Constructor initializes station and travel time data. */
    public Stations() {
        stationMap = new TreeMap<>();
        forwardTravelTimeMap = new TreeMap<>();
        backwardTravelTimeMap = new TreeMap<>();
        initializeStations();
        initializeTravelTimes();
    }

    /** Initializes the station map. */
    private void initializeStations() {
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

    /** Initializes the travel time maps. */
    private void initializeTravelTimes() {
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

    /** @return a defensive copy of the station map */
    public TreeMap<Integer, String> getStationMap() {
        return new TreeMap<>(stationMap); // Defensive copy for immutability
    }

    /** @return a defensive copy of the forward travel time map */
    public TreeMap<String, Integer> getForwardTravelTimeMap() {
        return new TreeMap<>(forwardTravelTimeMap); // Defensive copy
    }

    /** @return a defensive copy of the backward travel time map */
    public TreeMap<String, Integer> getBackwardTravelTimeMap() {
        return new TreeMap<>(backwardTravelTimeMap); // Defensive copy
    }
}
