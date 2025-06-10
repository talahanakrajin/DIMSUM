import java.util.TreeMap;
import java.util.ArrayList;

/**
 * Represents a dynamic schedule for a specific station.
 * Demonstrates use of Java Collections and encapsulation.
 */
public class CurrentStation extends Stations {
    private final String stationName;
    private final TreeMap<Integer, ArrayList<MRT>> schedule = new TreeMap<>();

    public CurrentStation(String stationName) {
        super(); // Initialize static station/travel time data
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    /** Add a train to the station's schedule. */
    public void addTrain(MRT train) {
        schedule.computeIfAbsent(train.getDepartureTime(), k -> new ArrayList<>()).add(train);
    }

    /** Remove a train from the station's schedule. */
    public void removeTrain(MRT train) {
        ArrayList<MRT> trains = schedule.get(train.getDepartureTime());
        if (trains != null) {
            trains.remove(train);
            if (trains.isEmpty()) {
                schedule.remove(train.getDepartureTime());
            }
        }
    }

    /** @return the next train scheduled at this station */
    public MRT getNextTrain() {
        return schedule.isEmpty() ? null : schedule.firstEntry().getValue().get(0);
    }

    /** @return a defensive copy of the station's schedule */
    public TreeMap<Integer, ArrayList<MRT>> getSchedule() {
        TreeMap<Integer, ArrayList<MRT>> copy = new TreeMap<>();
        for (var entry : schedule.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }
}
