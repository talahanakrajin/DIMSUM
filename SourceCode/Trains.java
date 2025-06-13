package SourceCode;
/**
 * Abstract base class for all train types in the MRT system.
 * This class defines the common properties and behaviors for all trains,
 * including train identification, scheduling, and state management.
 * Demonstrates use of inheritance, polymorphism, interfaces, and primitive data.
 */
public abstract class Trains implements Schedulable {
    /** Unique identifier for the train (e.g., TS1234) */
    private String trainID;
    
    /** Departure time in HHMM format */
    private int departureTime;
    
    /** Name of the current station */
    private String currentStation;
    
    /** Current delay in minutes */
    private int delay = 0;
    
    /** Track previous station for direction determination */
    private int previousStationNumber;

    /**
     * Constructor for Trains.
     * @param trainID Unique identifier for the train (must be in format TSxxxx)
     * @param departureTime Departure time in HHMM format
     * @param currentStation Name of the current station
     * @throws IllegalArgumentException if trainID format is invalid
     */
    public Trains(String trainID, int departureTime, String currentStation) {
        this.trainID = trainID; // Assume already validated and uppercase
        this.departureTime = departureTime;
        this.currentStation = currentStation;
        this.delay = 0;
        this.previousStationNumber = -1; // Initialize to -1 to indicate no previous station
    }

    /** @return The train's unique ID */
    public String getTrainID() { return trainID; }

    /** @return The current station name */
    public String getCurrentStation() { return currentStation; }

    /** @return The current delay in minutes */
    public int getDelay() { return delay; }

    /** @return The previous station number */
    public int getPreviousStationNumber() { return previousStationNumber; }

    /**
     * Sets the current station and updates the previous station number.
     * @param station The new current station
     */
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

    /** 
     * Sets the delay in minutes.
     * @param delay The new delay duration
     */
    public void setDelay(int delay) { this.delay = delay; }

    /**
     * Abstract method for simulating the journey of the train.
     * Must be implemented by concrete train classes.
     * @param stations Station data for the system
     * @param forward Initial direction of travel
     * @param steps Number of steps to simulate
     */
    public abstract void simulateJourney(Stations stations, boolean forward, int steps);

    /**
     * Abstract method to get the direction of the train.
     * Must be implemented by concrete train classes.
     * @return The direction as a string
     */
    public abstract String getDirection();

    /**
     * Sets the departure time (from Schedulable interface).
     * @param departureTime The new departure time
     */
    @Override
    public void setDepartureTime(int departureTime) { this.departureTime = departureTime; }

    /**
     * Gets the departure time (from Schedulable interface).
     * @return The current departure time
     */
    @Override
    public int getDepartureTime() { return departureTime; }

    /**
     * Returns a string representation of the train.
     * @return Formatted string with train details
     */
    @Override
    public String toString() {
        int hours = departureTime / 100;
        int minutes = departureTime % 100;
        String depTime = String.format("%02d:%02d", hours, minutes);
        return "TrainID: " + trainID + ", Departure: " + depTime +
                ", Station: " + currentStation;
    }
}
