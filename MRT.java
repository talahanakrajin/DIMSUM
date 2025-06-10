/**
 * MRT is a concrete train type, extending Trains.
 * Demonstrates inheritance and polymorphism.
 */
public class MRT extends Trains {
    private boolean isNorthbound; // Track direction

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

    public String getDirection() {
        if (isNorthbound) {
            return "Northbound (Lebak Bulus → Bundaran HI)";
        } else {
            return "Southbound (Bundaran HI → Lebak Bulus)";
        }
    }

    /**
     * Simulate this train moving back and forth through the system, creating new schedules,
     * until the given closing time is reached.
     * Each time the train reaches a new station, it is added to the global schedule.
     *
     * @param stations Stations object containing station and travel time data
     * @param forward  Initial direction (true for forward, false for backward)
     * @param closingTime The time (HHMM) after which simulation stops
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

        while (time <= closingTime) {
            // Add this train's current state to the global schedule with current direction
            MRT newTrain = new MRT(getTrainID(), time, stations.getStationMap().get(currentKey), dir);
            newTrain.setDelay(getDelay()); // Preserve delay
            MRTManager.addTrain(newTrain);

            // Determine next station and travel time
            int nextKey = dir ? currentKey + 1 : currentKey - 1;
            // Reverse direction at ends
            if (!stations.getStationMap().containsKey(nextKey)) {
                dir = !dir;
                this.isNorthbound = dir; // Update direction when reversing
                continue;
            }

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
}

