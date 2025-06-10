import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public final class MRTManager {
    // Maps train ID to train object for quick lookup
    private static final Map<String, Schedulable> trainById = new HashMap<>();
    
    // Maps station name to station object for quick lookup
    private static final Map<String, CurrentStation> stations = new HashMap<>();
    
    // MAIN SCHEDULE: departure time -> (train ID -> train)
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

    private MRTManager() {} // Prevent instantiation

    /**
     * Adds a train to the system.
     * Updates all necessary data structures to maintain consistency.
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
    }

    /**
     * Prints all train schedules in the system.
     * Time Complexity: O(n) where n is total number of trains
     * Space Complexity: O(1) for printing
     */
    public static void printAllSchedules() {
        for (var timeEntry : mainSchedule.entrySet()) {
            TreeMap<String, Schedulable> trainsAtTime = timeEntry.getValue();
            for (var trainEntry : trainsAtTime.entrySet()) {
                Schedulable schedule = trainEntry.getValue();
                if (schedule instanceof MRT mrt) {
                    String direction = mrt.getDirection();
                    String destination = direction.contains("Northbound") ? "Bundaran HI" : "Lebak Bulus";
                    if (mrt.getDelay() == 0) {
                        System.out.println(mrt + " | Heading To: " + destination);
                    } else {
                        System.out.println(mrt + " | Delayed: " + mrt.getDelay() + " min | Heading To: " + destination);
                    }
                }
            }
        }
    }

    /**
     * Prints the schedule for a specific station.
     * Uses station-specific schedule for O(1) lookup.
     * Time Complexity: O(m) where m is number of trains at the station
     */
    public static void printStationSchedule(Scanner sc, TreeMap<Integer, String> stationMap) {
        // Show station list and get user selection
        StationUtils.printStationList(stationMap);
        System.out.print("Enter the station number: ");
        int stationNum = StationUtils.stationSelection(sc, stationMap);
        String station = StationUtils.getStationName(stationNum, stationMap);
        StationUtils.printStationScheduleHeader(station);
        
        // Get station schedule directly from stationSchedule map
        TreeMap<Integer, TreeMap<String, MRT>> stationScheduleMap = stationSchedule.get(station);
        if (stationScheduleMap == null || stationScheduleMap.isEmpty()) {
            System.out.println("No trains for station " + station);
            return;
        }

        // Print all trains at this station
        for (var timeEntry : stationScheduleMap.entrySet()) {
            TreeMap<String, MRT> trainsAtTime = timeEntry.getValue();
            for (var trainEntry : trainsAtTime.entrySet()) {
                MRT train = trainEntry.getValue();
                String direction = train.getDirection();
                String destination = direction.contains("Northbound") ? "Bundaran HI" : "Lebak Bulus";
                if (train.getDelay() == 0) {
                    System.out.println(train + " | Heading To: " + destination);
                } else {
                    System.out.println(train + " | Delayed: " + train.getDelay() + " min | Heading To: " + destination);
                }
            }
        }
    }

    /**
     * Gets the next train in the system.
     */
    public static Schedulable getNextTrain() {
        return earliestTrain;
    }

    /**
     * Gets the next train at a specific station.
     */
    public static Schedulable getNextTrain(String stationName) {
        CurrentStation station = stations.get(stationName);
        return (station == null) ? null : station.getNextTrain();
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
    public static boolean delayTrain(String trainID, int departureTime, int delayMinutes) {
        Schedulable schedule = getTrainByIdAndTime(trainID, departureTime);
        if (schedule == null) return false;
        
        Trains train = (Trains) schedule;
        
        // Remove from old schedules
        removeFromSchedules(train);
        
        // Update delay and departure time
        train.setDelay(train.getDelay() + delayMinutes);
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
     * Prints all delayed trains sorted by delay time.
     * Time Complexity: O(n log n) where n is number of delayed trains
     */
    public static void printDelayedTrainsByPriority() {
        PriorityQueue<Schedulable> copy = new PriorityQueue<>(delayQueue);
        while (!copy.isEmpty()) {
            Trains train = (Trains) copy.poll();
            if (train instanceof MRT mrt) {
                String direction = mrt.getDirection();
                String destination = direction.contains("Northbound") ? "Bundaran HI" : "Lebak Bulus";
                System.out.println(train + " | Delayed: " + train.getDelay() + " min | Heading To: " + destination);
            } else {
                System.out.println(train + " | Delayed: " + train.getDelay() + " min");
            }
        }
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

    public static void getNextTrainAtStation(Scanner sc, TreeMap<Integer, String> stationMap) {
        StationUtils.printStationList(stationMap);
        int stationNum = StationUtils.stationSelection(sc, stationMap);
        String station = StationUtils.getStationName(stationNum, stationMap);
        
        CurrentStation currentStation = stations.get(station);
        Schedulable nextTrain = (currentStation == null) ? null : currentStation.getNextTrain();
        if (nextTrain != null) {
            System.out.println("Next train at " + station + ": " + nextTrain);
        } else {
            System.out.println("No trains found for station: " + station);
        }
    }
}
