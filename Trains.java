/**
 * Abstract base class for all train types.
 * Demonstrates use of inheritance, polymorphism, interfaces, and primitive data.
 */
public abstract class Trains implements Schedulable {
    // Instance variables (object state)
    private String trainID;
    private int departureTime; // primitive data
    private String currentStation;
    private int delay = 0; // primitive data
    private int previousStationNumber; // Track previous station for direction

    /**
     * Constructor for Trains.
     * @param trainID Unique identifier for the train. Must be in format TSxxxx.
     * @param departureTime Departure time in HHMM format.
     * @param currentStation Name of the current station.
     * @throws IllegalArgumentException if trainID format is invalid.
     */
    public Trains(String trainID, int departureTime, String currentStation) {
        this.trainID = trainID; // Assume already validated and uppercase
        this.departureTime = departureTime;
        this.currentStation = currentStation;
        this.delay = 0;
        this.previousStationNumber = -1; // Initialize to -1 to indicate no previous station
    }

    /** @return the train's unique ID */
    public String getTrainID() { return trainID; }

    /** @return the current station name */
    public String getCurrentStation() { return currentStation; }

    /** @return the delay in minutes */
    public int getDelay() { return delay; }

    /** @return the previous station number */
    public int getPreviousStationNumber() { return previousStationNumber; }

    /** Set the current station */
    public void setCurrentStation(String station) {
        // Update previous station number before changing current station
        for (var entry : new Stations().getStationMap().entrySet()) {
            if (entry.getValue().equals(this.currentStation)) {
                this.previousStationNumber = entry.getKey();
                break;
            }
        }
        this.currentStation = station;
    }

    /** Set the delay in minutes */
    public void setDelay(int delay) { this.delay = delay; }

    /** Abstract method for simulating the journey of the train */
    public abstract void simulateJourney(Stations stations, boolean forward, int steps);

    // Remove schedule/global management abstract methods.
    // Only keep instance-level methods relevant to a single train.

    /** Set the departure time (from Schedulable interface) */
    @Override
    public void setDepartureTime(int departureTime) { this.departureTime = departureTime; }

    /** Get the departure time (from Schedulable interface) */
    @Override
    public int getDepartureTime() { return departureTime; }

    /** String representation of the train */
    @Override
    public String toString() {
        int hours = departureTime / 100;
        int minutes = departureTime % 100;
        String depTime = String.format("%02d:%02d", hours, minutes);
        return "TrainID: " + trainID + ", Departure: " + depTime +
                ", Station: " + currentStation;
    }
}
