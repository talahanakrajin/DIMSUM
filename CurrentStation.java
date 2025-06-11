import java.util.TreeMap;
import java.util.ArrayList;

/**
 * Represents a dynamic schedule for a specific station.
 * Demonstrates use of Java Collections and encapsulation.
 */
public class CurrentStation extends Stations {
    private final String stationName;
    // Separate queues for each direction
    private final TreeMap<Integer, ArrayList<MRT>> northboundTrains = new TreeMap<>();
    private final TreeMap<Integer, ArrayList<MRT>> southboundTrains = new TreeMap<>();

    public CurrentStation(String stationName) {
        super(); // Initialize static station/travel time data
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    /** Add a train to the station's schedule. */
    public void addTrain(MRT train) {
        if (train.isNorthbound()) {
            northboundTrains.computeIfAbsent(train.getDepartureTime(), k -> new ArrayList<>()).add(train);
        } else {
            southboundTrains.computeIfAbsent(train.getDepartureTime(), k -> new ArrayList<>()).add(train);
        }
    }

    /** Remove a train from the station's schedule. */
    public void removeTrain(MRT train) {
        TreeMap<Integer, ArrayList<MRT>> directionMap = train.isNorthbound() ? 
            northboundTrains : southboundTrains;
            
        ArrayList<MRT> trains = directionMap.get(train.getDepartureTime());
        if (trains != null) {
            trains.remove(train);
            if (trains.isEmpty()) {
                directionMap.remove(train.getDepartureTime());
            }
        }
    }

    /** @return a defensive copy of the station's schedule */
    public TreeMap<Integer, ArrayList<MRT>> getSchedule() {
        TreeMap<Integer, ArrayList<MRT>> combined = new TreeMap<>();
        // Add northbound trains
        for (var entry : northboundTrains.entrySet()) {
            combined.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        // Add southbound trains
        for (var entry : southboundTrains.entrySet()) {
            combined.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return combined;
    }

    /** @return a defensive copy of the station's northbound schedule */
    public TreeMap<Integer, ArrayList<MRT>> getNorthboundSchedule() {
        TreeMap<Integer, ArrayList<MRT>> copy = new TreeMap<>();
        for (var entry : northboundTrains.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    /** @return a defensive copy of the station's southbound schedule */
    public TreeMap<Integer, ArrayList<MRT>> getSouthboundSchedule() {
        TreeMap<Integer, ArrayList<MRT>> copy = new TreeMap<>();
        for (var entry : southboundTrains.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    /** 
     * Gets the next train from either direction with the lowest departure time.
     * @return the next train, or null if no trains are scheduled
     */
    public MRT getNextTrain() {
        if (northboundTrains.isEmpty() && southboundTrains.isEmpty()) {
            return null;
        }
        
        // Get earliest times from both directions
        Integer northTime = northboundTrains.isEmpty() ? null : northboundTrains.firstKey();
        Integer southTime = southboundTrains.isEmpty() ? null : southboundTrains.firstKey();
        
        // If one direction is empty, return the other
        if (northTime == null) return southboundTrains.firstEntry().getValue().get(0);
        if (southTime == null) return northboundTrains.firstEntry().getValue().get(0);
        
        // Return the train with the earliest departure time
        return northTime <= southTime ? 
            northboundTrains.firstEntry().getValue().get(0) : 
            southboundTrains.firstEntry().getValue().get(0);
    }

    /**
     * Gets the next northbound train.
     * @return the next northbound train, or null if none exists
     */
    public Schedulable getNextNorthboundTrain() {
        if (northboundTrains.isEmpty()) {
            return null;
        }
        return northboundTrains.firstEntry().getValue().get(0);
    }

    /**
     * Gets the next southbound train.
     * @return the next southbound train, or null if none exists
     */
    public Schedulable getNextSouthboundTrain() {
        if (southboundTrains.isEmpty()) {
            return null;
        }
        return southboundTrains.firstEntry().getValue().get(0);
    }
}
