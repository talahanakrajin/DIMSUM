/**
 * Abstract base class for all train types.
 * Demonstrates use of inheritance, polymorphism, interfaces, and primitive data.
 */
public abstract class Trains implements Schedulable {
    // Instance variables (object state)
    private final String trainID;
    private int departureTime; // primitive data
    private String currentStation;
    private int delay = 0; // primitive data

    /**
     * Constructor for Trains.
     * @param trainID Unique identifier for the train.
     * @param departureTime Departure time in HHMM format.
     * @param currentStation Name of the current station.
     */
    public Trains(String trainID, int departureTime, String currentStation) {
        this.trainID = trainID;
        this.departureTime = departureTime;
        this.currentStation = currentStation;
    }

    /** @return the train's unique ID */
    public String getTrainID() { return trainID; }

    /** @return the current station name */
    public String getCurrentStation() { return currentStation; }

    /** @return the delay in minutes */
    public int getDelay() { return delay; }

    /** Set the current station */
    public void setCurrentStation(String station) { this.currentStation = station; }

    /** Set the delay in minutes */
    public void setDelay(int delay) { this.delay = delay; }

    /** Abstract method for train operation (polymorphism) */
    public abstract void operate();

    /** Set the departure time (from Schedulable interface) */
    @Override
    public void setDepartureTime(int departureTime) { this.departureTime = departureTime; }

    /** Get the departure time (from Schedulable interface) */
    @Override
    public int getDepartureTime() { return departureTime; }

    /** String representation of the train */
    @Override
    public String toString() {
        return "TrainID: " + trainID + ", Departure: " + String.format("%04d", departureTime) +
                ", Station: " + currentStation;
    }
}
