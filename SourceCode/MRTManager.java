package SourceCode;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.ArrayList;

public final class MRTManager {
    // Maps train ID to train object for quick lookup (e.g., "MRT123" -> MRT object)
    private static final Map<String, Schedulable> trainById = new HashMap<>();
    
    // Maps station name to station object for quick lookup (e.g., "Lebak Bulus" -> CurrentStation object)
    private static final Map<String, CurrentStation> stations = new HashMap<>();
    
    // MAIN SCHEDULE: departure time -> (train ID -> train object)
    // Using TreeMap for both levels ensures trains are sorted by time and then by ID
    private static final TreeMap<Integer, TreeMap<String, Schedulable>> mainSchedule = new TreeMap<>();
    
    // Station-specific schedules: station name -> (departure time -> (train ID -> train))
    private static final Map<String, TreeMap<Integer, TreeMap<String, MRT>>> stationSchedule = new HashMap<>();
    
    // Track the earliest train for quick access
    private static Schedulable earliestTrain = null;
    
    // Queue for delayed trains, sorted by delay time
    private static final PriorityQueue<Schedulable> delayQueue = new PriorityQueue<>(
        (a, b) -> {
            int cmp = Integer.compare(((Trains) b).getDelay(), ((Trains) a).getDelay());
            if (cmp == 0) {
                return Integer.compare(a.getDepartureTime(), b.getDepartureTime());
            }
            return cmp;
        }
    );

    private static Schedulable earliestNorthboundTrain = null;
    private static Schedulable earliestSouthboundTrain = null;

    private MRTManager() {} // Prevent instantiation
    
    // Earlist trains update methods to be used for O(1) lookups in getNextTrain()
    /**
     * Updates the earliest trains when a new train is added.
     */
    private static void updateEarliestTrains(Schedulable train) {
        if (train instanceof MRT mrt) {
            if (mrt.isNorthbound()) {
                if (earliestNorthboundTrain == null || mrt.isEarlierThan(earliestNorthboundTrain)) {
                    earliestNorthboundTrain = train;
                }
            } else {
                if (earliestSouthboundTrain == null || mrt.isEarlierThan(earliestSouthboundTrain)) {
                    earliestSouthboundTrain = train;
                }
            }
        }
    }
    /**
     * Updates the earliest trains when a train is removed.
     */
    private static void updateEarliestTrainsAfterRemoval(Schedulable train) {
        if (train instanceof MRT mrt) {
            if (mrt.isNorthbound() && train == earliestNorthboundTrain) {
                // Find new earliest northbound train
                earliestNorthboundTrain = null;
                for (var station : stations.values()) {
                    Schedulable next = station.getNextNorthboundTrain();
                    if (next instanceof MRT nextTrain && 
                        (earliestNorthboundTrain == null || nextTrain.isEarlierThan(earliestNorthboundTrain))) {
                        earliestNorthboundTrain = next;
                    }
                }
            } else if (!mrt.isNorthbound() && train == earliestSouthboundTrain) {
                // Find new earliest southbound train
                earliestSouthboundTrain = null;
                for (var station : stations.values()) {
                    Schedulable next = station.getNextSouthboundTrain();
                    if (next instanceof MRT nextTrain && 
                        (earliestSouthboundTrain == null || nextTrain.isEarlierThan(earliestSouthboundTrain))) {
                        earliestSouthboundTrain = next;
                    }
                }
            }
        }
    }

    /**
     * Adds a train to the system.
     * Updates all necessary data structures to maintain consistency.
     * @param schedule the train to add
     */
    public static void addTrain(Schedulable schedule) {
        int dep = schedule.getDepartureTime();
        String trainId = ((Trains) schedule).getTrainID();
        String station = ((Trains) schedule).getCurrentStation();
        
        // Add to global schedule
        mainSchedule.computeIfAbsent(dep, k -> new TreeMap<>()).put(trainId, schedule);
        
        // Add to train lookup
        trainById.put(trainId, schedule);
        
        // Add to station
        stations.computeIfAbsent(station, k -> new CurrentStation(k)).addTrain((MRT) schedule);
        
        // Add to station-specific schedule
        if (schedule instanceof MRT mrt) {
            stationSchedule.computeIfAbsent(station, k -> new TreeMap<>())
                .computeIfAbsent(dep, k -> new TreeMap<>())
                .put(trainId, mrt);
        }

        // Update earliest train if needed
        if (earliestTrain == null || dep < earliestTrain.getDepartureTime()) {
            earliestTrain = schedule;
        }

        updateEarliestTrains(schedule);
    }

    /**
     * Displays a train's information.
     * @param train the train to display
     * @param stationName the station name (null for system-wide display)
     * @param isFirstTrain whether this is the first train in a group (to print headers)
     */
    public static void displayTrainSchedule(Schedulable train, String stationName, boolean isFirstTrain) {
        if (train == null) {
            if (stationName == null) {
                System.out.println("No trains found in the system.");
            } else {
                System.out.println("No trains found for station: " + stationName);
            }
            return;
        }

        if (train instanceof MRT mrt) {
            String direction = mrt.getDirection();
            String destination = direction.contains("Northbound") ? "Bundaran HI" : "Lebak Bulus";
            
            if (isFirstTrain) {
                System.out.println("\nHeading To: " + destination);
                System.out.println("-------------------------------");
            }
            
            String timeStr = String.format("%02d:%02d", train.getDepartureTime() / 100, train.getDepartureTime() % 100);
            System.out.printf("%s - %s - %s%n",
                timeStr,
                mrt.getTrainID(),
                mrt.getCurrentStation()
            );
        }
    }

    /**
     * Prints all train schedules in chronological order, grouped by direction.
     * Shows all trains heading to Bundaran HI first, then all trains heading to Lebak Bulus.
     * Each group shows schedules in chronological order.
     */
    public static void printAllSchedules() {
        if (mainSchedule.isEmpty()) {
            MRT.displayNoTrainsMessage(null);
            return;
        }

        // First, show all trains heading to Bundaran HI
        System.out.println("\nHeading To: Bundaran HI");
        System.out.println("-------------------------------");
        boolean hasNorthbound = false;
        for (var entry : mainSchedule.entrySet()) {
            var trains = entry.getValue();
            for (var train : trains.values()) {
                if (train instanceof MRT mrt && mrt.getDirection().contains("Bundaran HI")) {
                    hasNorthbound = true;
                    mrt.displaySchedule(null, false);
                }
            }
        }
        if (!hasNorthbound) {
            System.out.println("No trains heading to Bundaran HI");
        }
        System.out.println("-------------------------------");

        // Then, show all trains heading to Lebak Bulus
        System.out.println("\nHeading To: Lebak Bulus");
        System.out.println("-------------------------------");
        boolean hasSouthbound = false;
        for (var entry : mainSchedule.entrySet()) {
            var trains = entry.getValue();
            for (var train : trains.values()) {
                if (train instanceof MRT mrt && mrt.getDirection().contains("Lebak Bulus")) {
                    hasSouthbound = true;
                    mrt.displaySchedule(null, false);
                }
            }
        }
        if (!hasSouthbound) {
            System.out.println("No trains heading to Lebak Bulus");
        }
        System.out.println("-------------------------------");
    }

    /**
     * Prints the train schedule for a specific station.
     * Shows trains in both directions except at terminus stations.
     * For example:
     * - At Lebak Bulus: only shows trains heading to Bundaran HI
     * - At Bundaran HI: only shows trains heading to Lebak Bulus
     * - At other stations: shows trains in both directions
     * 
     * @param stationName The name of the station to show schedules for
     */
    public static void printStationSchedule(String stationName) {
        CurrentStation station = stations.get(stationName);
        if (station == null) {
            MRT.displayNoTrainsMessage(stationName);
            return;
        }

        StationUtils.printStationScheduleHeader(stationName);
        
        // Get and display northbound trains
        TreeMap<Integer, ArrayList<MRT>> northboundSchedule = station.getNorthboundSchedule();
        if (!northboundSchedule.isEmpty()) {
            boolean isFirst = true;
            for (var timeEntry : northboundSchedule.entrySet()) {
                for (MRT train : timeEntry.getValue()) {
                    // At Lebak Bulus, only show trains heading to Bundaran HI
                    if (stationName.equals("Lebak Bulus") && !train.isNorthbound()) continue;
                    // At Bundaran HI, only show trains heading to Lebak Bulus
                    if (stationName.equals("Bundaran HI") && train.isNorthbound()) continue;
                    
                    train.displaySchedule(stationName, isFirst);
                    isFirst = false;
                }
            }
            System.out.println("-------------------------------");
        }

        // Get and display southbound trains
        TreeMap<Integer, ArrayList<MRT>> southboundSchedule = station.getSouthboundSchedule();
        if (!southboundSchedule.isEmpty()) {
            boolean isFirst = true;
            for (var timeEntry : southboundSchedule.entrySet()) {
                for (MRT train : timeEntry.getValue()) {
                    // At Lebak Bulus, only show trains heading to Bundaran HI
                    if (stationName.equals("Lebak Bulus") && !train.isNorthbound()) continue;
                    // At Bundaran HI, only show trains heading to Lebak Bulus
                    if (stationName.equals("Bundaran HI") && train.isNorthbound()) continue;
                    
                    train.displaySchedule(stationName, isFirst);
                    isFirst = false;
                }
            }
            System.out.println("-------------------------------");
        }

        // If no trains are scheduled for this station, show a message
        if (northboundSchedule.isEmpty() && southboundSchedule.isEmpty()) {
            MRT.displayNoTrainsMessage(stationName);
        }
    }

    /**
     * Gets the current time in HHMM format.
     * @return current time as integer (e.g., 1430 for 2:30 PM)
     */
    private static int getCurrentTime() {
        java.time.LocalTime now = java.time.LocalTime.now();
        int time = now.getHour() * 100 + now.getMinute();
        //System.out.println("DEBUG: Current time is " + time);
        return time;
    }

    /**
     * Gets the next train in the entire system.
     * Shows both the earliest northbound and southbound trains if they exist.
     * Only shows trains that haven't departed yet based on current time.
     */
    public static void getNextTrain() {
        int currentTime = getCurrentTime();
        
        // If no trains are scheduled, show a message
        if (mainSchedule.isEmpty()) {
            MRT.displayNoTrainsMessage(null);
            return;
        }

        boolean hasFutureTrains = false;
        boolean isFirstNorthbound = true;
        boolean isFirstSouthbound = true;

        // Find next available northbound train
        for (var timeEntry : mainSchedule.entrySet()) {
            int time = timeEntry.getKey();
            if (time >= currentTime) {
                for (var train : timeEntry.getValue().values()) {
                    if (train instanceof MRT mrt && mrt.isNorthbound()) {
                        mrt.displaySchedule(null, isFirstNorthbound);
                        System.out.println("-------------------------------");
                        hasFutureTrains = true;
                        isFirstNorthbound = false;
                        break;
                    }
                }
                break;
            }
        }

        // Find next available southbound train
        for (var timeEntry : mainSchedule.entrySet()) {
            int time = timeEntry.getKey();
            if (time >= currentTime) {
                for (var train : timeEntry.getValue().values()) {
                    if (train instanceof MRT mrt && !mrt.isNorthbound()) {
                        mrt.displaySchedule(null, isFirstSouthbound);
                        System.out.println("-------------------------------");
                        hasFutureTrains = true;
                        isFirstSouthbound = false;
                        break;
                    }
                }
                break;
            }
        }

        // If no future trains are found, show a message
        if (!hasFutureTrains) {
            System.out.println("No more trains scheduled for today.");
        }
    }

    /**
     * Gets the next train at a specific station.
     * Shows both the earliest northbound and southbound trains if they exist.
     * Only shows trains that haven't departed yet based on current local time.
     * @param stationName The name of the station to check
     */
    public static void getNextTrain(String stationName) {
        int currentTime = getCurrentTime();
        
        // Get the station object from our stations map
        CurrentStation station = stations.get(stationName);
        if (station == null) {
            MRT.displayNoTrainsMessage(stationName);
            return;
        }

        boolean hasFutureTrains = false;
        boolean isFirstNorthbound = true;
        boolean isFirstSouthbound = true;

        // Get all trains from the station
        TreeMap<Integer, ArrayList<MRT>> northboundSchedule = station.getNorthboundSchedule();
        TreeMap<Integer, ArrayList<MRT>> southboundSchedule = station.getSouthboundSchedule();

        // Find next available northbound train
        for (var timeEntry : northboundSchedule.entrySet()) {
            int time = timeEntry.getKey();
            if (time >= currentTime) {
                for (MRT train : timeEntry.getValue()) {
                    train.displaySchedule(stationName, isFirstNorthbound);
                    System.out.println("-------------------------------");
                    hasFutureTrains = true;
                    isFirstNorthbound = false;
                    break;
                }
                break;
            }
        }

        // Find next available southbound train
        for (var timeEntry : southboundSchedule.entrySet()) {
            int time = timeEntry.getKey();
            if (time >= currentTime) {
                for (MRT train : timeEntry.getValue()) {
                    train.displaySchedule(stationName, isFirstSouthbound);
                    System.out.println("-------------------------------");
                    hasFutureTrains = true;
                    isFirstSouthbound = false;
                    break;
                }
                break;
            }
        }

        // If no future trains are found, show a message
        if (!hasFutureTrains) {
            System.out.println("No more trains scheduled for today at " + stationName);
        }
    }

    /**
     * Gets a train by its ID and departure time.
     * Uses TreeMap's ordering for efficient lookup.
     * @return The train if found, null otherwise
     */
    public static Schedulable getTrainByIdAndTime(String trainID, int departureTime) {
        TreeMap<String, Schedulable> trainsAtTime = mainSchedule.get(departureTime);
        if (trainsAtTime != null) {
            return trainsAtTime.get(trainID);
        }
        return null;
    }

    /**
     * Delays a train and updates its schedule.
     * @return true if train was found and delayed, false otherwise
     */
    public static boolean delayTrain(String trainID, int departureTime, int delayMinutes, String reason) {
        Schedulable schedule = getTrainByIdAndTime(trainID, departureTime);
        if (schedule == null) return false;
        
        Trains train = (Trains) schedule;
        
        // Remove from old schedules
        removeFromSchedules(train);
        
        // Update delay and departure time
        if (train instanceof MRT mrt) {
            mrt.setDelay(delayMinutes, reason);
        } else {
            train.setDelay(train.getDelay() + delayMinutes);
        }
        int newDepartureTime = TimeUtils.addMinutesToDepTime(train.getDepartureTime(), delayMinutes);
        train.setDepartureTime(newDepartureTime);
        
        // Add to delay queue
        delayQueue.remove(train);
        delayQueue.add(train);
        
        // Add back to schedules with new time
        addTrain(train);
        return true;
    }

    /**
     * Prints all delayed trains.
     * Shows trains grouped by direction, with delay information.
     */
    public static void printDelayedTrains() {
        if (delayQueue.isEmpty()) {
            System.out.println("No delayed trains.");
            return;
        }

        // First, show all delayed trains heading to Bundaran HI
        System.out.println("\nHeading To: Bundaran HI");
        System.out.println("-------------------------------");
        boolean hasNorthbound = false;
        for (var train : delayQueue) {
            if (train instanceof MRT mrt && mrt.isNorthbound()) {
                hasNorthbound = true;
                mrt.displaySchedule(null, !hasNorthbound);
            }
        }
        if (!hasNorthbound) {
            System.out.println("No delayed trains heading to Bundaran HI");
        }
        System.out.println("-------------------------------");

        // Then, show all delayed trains heading to Lebak Bulus
        System.out.println("\nHeading To: Lebak Bulus");
        System.out.println("-------------------------------");
        boolean hasSouthbound = false;
        for (var train : delayQueue) {
            if (train instanceof MRT mrt && !mrt.isNorthbound()) {
                hasSouthbound = true;
                mrt.displaySchedule(null, !hasSouthbound);
            }
        }
        if (!hasSouthbound) {
            System.out.println("No delayed trains heading to Lebak Bulus");
        }
        System.out.println("-------------------------------");
    }

    /**
     * Reschedules a train to a new departure time and station.
     * @return true if train was found and rescheduled, false otherwise
     */
    public static boolean rescheduleTrain(String trainID, int oldDepartureTime, int newDepartureTime, String newStation) {
        Schedulable schedule = getTrainByIdAndTime(trainID, oldDepartureTime);
        if (schedule == null) return false;
        
        Trains train = (Trains) schedule;
        
        // Remove from old schedules
        removeFromSchedules(train);
        
        // Update train properties
        train.setDepartureTime(newDepartureTime);
        train.setCurrentStation(newStation);
        
        // If it's an MRT, update its direction based on the new station
        if (train instanceof MRT mrt) {
            mrt.reschedule(newDepartureTime, newStation);
        }
        
        // Add to new schedules
        addTrain(train);
        return true;
    }

    /**
     * Cancels a train and removes it from all schedules.
     * @return true if train was found and cancelled, false otherwise
     */
    public static boolean cancelTrain(String trainID, int departureTime) {
        Schedulable schedule = getTrainByIdAndTime(trainID, departureTime);
        if (schedule == null) return false;
        
        // Remove from all schedules
        removeFromSchedules((Trains) schedule);
        
        // Remove from train lookup
        trainById.remove(trainID);
        
        // Remove from delay queue if present
        delayQueue.remove(schedule);
        
        // Update earliest train
        earliestTrain = mainSchedule.isEmpty() ? null : mainSchedule.firstEntry().getValue().values().stream().findFirst().orElse(null);
        return true;
    }

    /**
     * Helper method to remove a train from all schedules.
     */
    public static void removeFromSchedules(Trains train) {
        String trainId = train.getTrainID();
        int dep = train.getDepartureTime();
        String station = train.getCurrentStation();
        
        // Remove from global schedule
        TreeMap<String, Schedulable> trainsAtTime = mainSchedule.get(dep);
        if (trainsAtTime != null) {
            trainsAtTime.remove(trainId);
            if (trainsAtTime.isEmpty()) {
                mainSchedule.remove(dep);
            }
        }
        
        // Remove from station
        CurrentStation stationObj = stations.get(station);
        if (stationObj != null) stationObj.removeTrain((MRT) train);
        
        // Remove from station schedules
        TreeMap<Integer, TreeMap<String, MRT>> stationScheduleMap = stationSchedule.get(station);
        if (stationScheduleMap != null) {
            TreeMap<String, MRT> trainsAtTimeInStation = stationScheduleMap.get(dep);
            if (trainsAtTimeInStation != null) {
                trainsAtTimeInStation.remove(trainId);
                if (trainsAtTimeInStation.isEmpty()) {
                    stationScheduleMap.remove(dep);
                }
            }
        }

        updateEarliestTrainsAfterRemoval(train);
    }

    public static Schedulable getTrainById(String id) {
        return trainById.get(id);
    }

    public static void simulateTrainsRunning(int closingTime) {
        Stations stations = new Stations();
        // To avoid ConcurrentModificationException, copy the values to a list
        java.util.List<Schedulable> trains = new java.util.ArrayList<>(trainById.values());
        for (Schedulable schedule : trains) {
            if (schedule instanceof MRT mrt) {
                // Use the train's current station and departure time as starting point
                // Use a fresh MRT object for simulation to avoid overwriting the original
                String currentStation = mrt.getCurrentStation();
                int stationNum = mrt.getStationNumber(currentStation);
                boolean isNorthbound = stationNum < 13; // If not at Bundaran HI (13), train is northbound
                MRT simTrain = new MRT(mrt.getTrainID(), mrt.getDepartureTime(), currentStation, isNorthbound);
                simTrain.simulateJourney(stations, isNorthbound, closingTime);
            }
        }
    }
}




