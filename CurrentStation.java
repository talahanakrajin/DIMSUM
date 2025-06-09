import java.util.TreeMap;

/**
 * Represents a dynamic schedule for a specific station.
 * Demonstrates use of Java Collections and encapsulation.
 */
public class CurrentStation extends Stations {
    private final String stationName;
    private final TreeMap<Integer, MRT> schedule = new TreeMap<>();

    public CurrentStation(String stationName) {
        super(); // Initialize static station/travel time data
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    /** Add a train to the station's schedule. */
    public void addTrain(MRT train) {
        schedule.put(train.getDepartureTime(), train);
    }

    /** Remove a train from the station's schedule. */
    public void removeTrain(MRT train) {
        schedule.remove(train.getDepartureTime());
    }

    /** @return the next train scheduled at this station */
    public MRT getNextTrain() {
        return schedule.isEmpty() ? null : schedule.firstEntry().getValue();
    }

    /** @return a defensive copy of the station's schedule */
    public TreeMap<Integer, MRT> getSchedule() {
        return new TreeMap<>(schedule); // Defensive copy
    }
}
