/**
 * MRT is a concrete train type, extending Trains.
 * Demonstrates inheritance and polymorphism.
 */
public class MRT extends Trains {
    private boolean isNorthbound; // Track direction
    private boolean isDelayed = false;
    private int delayDuration = 0;
    private String delayReason = "";

    /**
     * Constructor for MRT.
     * @param trainID Unique identifier for the train.
     * @param departureTime Departure time in HHMM format.
     * @param currentStation Name of the current station.
     * @param isNorthbound Whether the train is heading northbound.
     */
    public MRT(String trainID, int departureTime, String currentStation, boolean isNorthbound) {
        super(trainID, departureTime, currentStation);
        this.isNorthbound = isNorthbound;
    }

    public void delay(int minutes) {
        setDelay(getDelay() + minutes);
    }

    public void reschedule(int newDepartureTime, String newStation) {
        setDepartureTime(newDepartureTime);
        setCurrentStation(newStation);
        // Update direction based on new station
        this.isNorthbound = StationUtils.checkInitialDirection(newStation);
    }

    public int getStationNumber(String stationName) {
        for (var entry : new Stations().getStationMap().entrySet()) {
            if (entry.getValue().equals(stationName)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    @Override
    public String getDirection() {
        return isNorthbound ? "Bundaran HI" : "Lebak Bulus";
    }

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

        while (time <= closingTime) {
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

            // If after moving, time exceeds closingTime, stop
            if (time > closingTime) break;
        }
    }

    /**
     * Displays the train schedule.
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
     * Checks if the train is delayed.
     * @return true if the train is delayed, false otherwise
     */
    public boolean isDelayed() {
        return isDelayed;
    }

    /**
     * Gets the delay duration in minutes.
     * @return the delay duration
     */
    public int getDelayDuration() {
        return delayDuration;
    }

    /**
     * Gets the reason for the delay.
     * @return the delay reason
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
     * Checks if the train is heading northbound.
     * @return true if the train is heading northbound, false otherwise
     */
    public boolean isNorthbound() {
        return isNorthbound;
    }

    /**
     * Checks if this train departs earlier than another train.
     * @param other The train to compare with
     * @return true if this train departs earlier, false otherwise
     */
    public boolean isEarlierThan(Schedulable other) {
        return getDepartureTime() < other.getDepartureTime();
    }

    /**
     * Gets the opposite terminal station.
     * @return "Bundaran HI" if heading to Lebak Bulus, "Lebak Bulus" if heading to Bundaran HI
     */
    public String getOppositeTerminal() {
        return isNorthbound() ? "Bundaran HI" : "Lebak Bulus";
    }
}

