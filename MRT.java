/**
 * MRT is a concrete train type that extends the abstract Trains class.
 * This class represents a Mass Rapid Transit train in the system.
 * It handles train-specific operations like journey simulation, delay management,
 * and schedule display. Demonstrates inheritance and polymorphism.
 */
public class MRT extends Trains {
    // Indicates whether the train is traveling northbound 
    private boolean isNorthbound;
    
    // Indicates if the train is currently delayed 
    private boolean isDelayed = false;
    
    // Duration of the delay in minutes 
    private int delayDuration = 0;
    
    // Reason for the delay 
    private String delayReason = "";

    /**
     * Constructor for MRT train.
     * @param trainID Unique identifier for the train
     * @param departureTime Departure time in HHMM format
     * @param currentStation Name of the current station
     * @param isNorthbound Whether the train is heading northbound
     */
    public MRT(String trainID, int departureTime, String currentStation, boolean isNorthbound) {
        super(trainID, departureTime, currentStation);
        this.isNorthbound = isNorthbound;
    }

    /**
     * Adds additional delay minutes to the train's current delay.
     * @param minutes Number of minutes to add to the current delay
     */
    public void delay(int minutes) {
        setDelay(getDelay() + minutes);
    }

    /**
     * Reschedules the train to a new departure time and station.
     * Updates the train's direction based on the new station.
     * @param newDepartureTime New departure time in HHMM format
     * @param newStation New station name
     */
    public void reschedule(int newDepartureTime, String newStation) {
        setDepartureTime(newDepartureTime);
        setCurrentStation(newStation);
        // Update direction based on new station
        this.isNorthbound = StationUtils.checkInitialDirection(newStation);
    }

    /**
     * Gets the station number for a given station name.
     * @param stationName Name of the station to look up
     * @return Station number, or -1 if not found
     */
    public int getStationNumber(String stationName) {
        for (var entry : new Stations().getStationMap().entrySet()) {
            if (entry.getValue().equals(stationName)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Displays the train schedule information.
     * Shows different formats based on whether the train is delayed.
     * @param stationName The station name to display (null for system-wide display)
     * @param isFirst Whether this is the first train in a list
     */
    public void displaySchedule(String stationName, boolean isFirst) {
        // If this is the first train in a list, show the direction header
        if (isFirst) {
            System.out.println("\nHeading To: " + getDirection());
            System.out.println("-------------------------------");
        }

        // Format time as HH:MM
        String timeStr = String.format("%02d:%02d", getDepartureTime() / 100, getDepartureTime() % 100);
        
        // Display train details in the original format
        if (isDelayed()) {
            System.out.printf("%s - %s - %s | Delayed: %d minutes (Reason: %s)%n",
                timeStr,
                getTrainID(),
                getCurrentStation(),
                getDelayDuration(),
                getDelayReason()
            );
        } else {
            System.out.printf("%s - %s - %s%n",
                timeStr,
                getTrainID(),
                getCurrentStation()
            );
        }
    }

    /**
     * Displays a message when no trains are available.
     * @param stationName The station name to display (null for system-wide display)
     */
    public static void displayNoTrainsMessage(String stationName) {
        if (stationName != null) {
            System.out.println("No trains scheduled for station: " + stationName);
        } else {
            System.out.println("No trains scheduled in the system.");
        }
    }

    /**
     * @return true if the train is delayed, false otherwise
     */
    public boolean isDelayed() {
        return isDelayed;
    }

    /**
     * @return The delay duration in minutes
     */
    public int getDelayDuration() {
        return delayDuration;
    }

    /**
     * @return The reason for the delay
     */
    public String getDelayReason() {
        return delayReason;
    }

    /**
     * Sets the delay information for the train.
     * @param duration The delay duration in minutes
     * @param reason The reason for the delay
     */
    public void setDelay(int duration, String reason) {
        this.isDelayed = true;
        this.delayDuration = duration;
        this.delayReason = reason;
    }

    /**
     * Clears the delay information for the train.
     */
    public void clearDelay() {
        this.isDelayed = false;
        this.delayDuration = 0;
        this.delayReason = "";
    }

    /**
     * @return true if the train is heading northbound, false otherwise
     */
    public boolean isNorthbound() {
        return isNorthbound;
    }

    /**
     * Compares this train's departure time with another train.
     * @param other The train to compare with
     * @return true if this train departs earlier, false otherwise
     */
    public boolean isEarlierThan(Schedulable other) {
        return getDepartureTime() < other.getDepartureTime();
    }

    /**
     * @return The opposite terminal station name based on current direction
     */
    public String getOppositeTerminal() {
        return isNorthbound() ? "Bundaran HI" : "Lebak Bulus";
    }
    
    /**
     * @return The direction of the train as a string
     */
    @Override
    public String getDirection() {
        return isNorthbound ? "Bundaran HI" : "Lebak Bulus";
    }

    /**
     * Simulates the train's journey through the system.
     * Handles station-to-station movement, direction changes at terminals,
     * and time calculations including delays.
     * @param stations Station data for the system
     * @param forward Initial direction of travel
     * @param closingTime System closing time in HHMM format
     */
    @Override
    public void simulateJourney(Stations stations, boolean forward, int closingTime) {
        int currentKey = -1;
        // Find the current station key
        for (var entry : stations.getStationMap().entrySet()) {
            if (entry.getValue().equalsIgnoreCase(getCurrentStation())) {
                currentKey = entry.getKey();
                break;
            }
        }
        if (currentKey == -1) return;

        int time = getDepartureTime();
        boolean dir = forward;
        this.isNorthbound = dir; // Set initial direction

        while (true) {
            // Check if we've passed the closing time
            // If current time is less than closing time, we've wrapped around midnight
            if (time > closingTime && time < 2400) {
                break;
            }

            // Determine next station and travel time
            int nextKey = dir ? currentKey + 1 : currentKey - 1;
            // Reverse direction at ends
            if (!stations.getStationMap().containsKey(nextKey)) {
                dir = !dir;
                this.isNorthbound = dir; // Update direction when reversing
                nextKey = dir ? currentKey + 1 : currentKey - 1; // Recalculate nextKey after direction change
                if (!stations.getStationMap().containsKey(nextKey)) break; // Safety check
            }

            // Add this train's current state to the global schedule with current direction
            MRT newTrain = new MRT(getTrainID(), time, stations.getStationMap().get(currentKey), dir);
            newTrain.setDelay(getDelay()); // Preserve delay
            MRTManager.addTrain(newTrain);

            String travelKey = currentKey + "-" + nextKey;
            Integer travelTime = dir
                ? stations.getForwardTravelTimeMap().get(travelKey)
                : stations.getBackwardTravelTimeMap().get(travelKey);

            if (travelTime == null) break;

            // Move to next station
            time = TimeUtils.addMinutesToDepTime(time, travelTime);
            currentKey = nextKey;

            // If we've wrapped around midnight, break the loop
            if (time < getDepartureTime()) break;
        }
    }
}

