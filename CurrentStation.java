/**
 * Represents a dynamic schedule for a specific station in the MRT system.
 * This class manages train schedules for both northbound and southbound directions
 * using TreeMap data structures for efficient time-based operations.
 * Demonstrates use of Java Collections and encapsulation.
 */
import java.util.TreeMap;
import java.util.ArrayList;

public class CurrentStation extends Stations {
    /** The name of this station */
    private final String stationName;
    
    /** 
     * TreeMap storing northbound trains, keyed by departure time.
     * Each time slot can have multiple trains (ArrayList).
     */
    private final TreeMap<Integer, ArrayList<MRT>> northboundTrains = new TreeMap<>();
    
    /** 
     * TreeMap storing southbound trains, keyed by departure time.
     * Each time slot can have multiple trains (ArrayList).
     */
    private final TreeMap<Integer, ArrayList<MRT>> southboundTrains = new TreeMap<>();

    /**
     * Constructor initializes a station with the given name.
     * @param stationName The name of the station
     */
    public CurrentStation(String stationName) {
        super(); // Initialize static station/travel time data
        this.stationName = stationName;
    }

    /**
     * @return The name of this station
     */
    public String getStationName() {
        return stationName;
    }

    /** 
     * Adds a train to the station's schedule.
     * The train is added to either northbound or southbound schedule based on its direction.
     * @param train The train to add to the schedule
     */
    public void addTrain(MRT train) {
        if (train.isNorthbound()) {
            northboundTrains.computeIfAbsent(train.getDepartureTime(), k -> new ArrayList<>()).add(train);
        } else {
            southboundTrains.computeIfAbsent(train.getDepartureTime(), k -> new ArrayList<>()).add(train);
        }
    }

    /** 
     * Removes a train from the station's schedule.
     * If the time slot becomes empty after removal, it is also removed.
     * @param train The train to remove from the schedule
     */
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

    /** 
     * @return A defensive copy of the station's complete schedule
     * containing both northbound and southbound trains
     */
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

    /** 
     * @return A defensive copy of the station's northbound schedule
     */
    public TreeMap<Integer, ArrayList<MRT>> getNorthboundSchedule() {
        TreeMap<Integer, ArrayList<MRT>> copy = new TreeMap<>();
        for (var entry : northboundTrains.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    /** 
     * @return A defensive copy of the station's southbound schedule
     */
    public TreeMap<Integer, ArrayList<MRT>> getSouthboundSchedule() {
        TreeMap<Integer, ArrayList<MRT>> copy = new TreeMap<>();
        for (var entry : southboundTrains.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    /** 
     * Gets the next train from either direction with the lowest departure time.
     * @return The next train, or null if no trains are scheduled
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
     * @return The next northbound train, or null if none exists
     */
    public Schedulable getNextNorthboundTrain() {
        if (northboundTrains.isEmpty()) {
            return null;
        }
        return northboundTrains.firstEntry().getValue().get(0);
    }

    /**
     * Gets the next southbound train.
     * @return The next southbound train, or null if none exists
     */
    public Schedulable getNextSouthboundTrain() {
        if (southboundTrains.isEmpty()) {
            return null;
        }
        return southboundTrains.firstEntry().getValue().get(0);
    }
}
