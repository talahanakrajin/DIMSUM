import java.util.*;

public class HybridScheduleManager {
    // Fast lookup by refering to trainID, using HashMap
    private final Map<String, HybridTrain> trainById = new HashMap<>();
    // Sorted by departure time, using TreeMap for O(log n) access
    private final Map<String, TreeMap<Integer, HybridTrain>> stationSchedules = new HashMap<>();

    // Global schedule sorted by departure time, using TreeMap for O(log n) access
    private final TreeMap<Integer, HybridTrain> globalSchedule = new TreeMap<>();
    // HybridTrain with the earliest departure time, for O(1) access to next train
    private HybridTrain earliestTrain = null;

    // Only keep delayQueue for delayed train priority (not for reschedule/cancel)
    // Highest delay = highest priority (front of the queue), then by earliest departure time
    private final PriorityQueue<HybridTrain> delayQueue = new PriorityQueue<>(
        (a, b) -> {
            // Descending order by delay: largest delay first
            int cmp = Integer.compare(b.getDelay(), a.getDelay());
            // If still equal, break ties by departure time (earlier departs first)
            if (cmp == 0) {
                return Integer.compare(a.getDepartureTime(), b.getDepartureTime());
            }
            return cmp;
        }
    );

    public void addTrain(HybridTrain train) {
        // Validate departure time is in HHMM format (00:00 to 23:59)
        int dep = train.getDepartureTime();
        int hours = dep / 100;
        int minutes = dep % 100;
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Departure time must be in HHMM format (0000 to 2359), got: " + dep);
        }

        globalSchedule.put(train.getDepartureTime(), train);
        trainById.put(train.getTrainID(), train);
        stationSchedules.computeIfAbsent(train.getStation(), k -> new TreeMap<>()).put(train.getDepartureTime(), train);
        
        if (earliestTrain == null || train.getDepartureTime() < earliestTrain.getDepartureTime()) {
            earliestTrain = train;
        }
    }

    // Print all schedules (sorted by time)
    public void printAllSchedules() {
        // This prints trains in order of departure time (from globalSchedule TreeMap),
        for (var entry : globalSchedule.entrySet()) {
            HybridTrain train = entry.getValue();
            if (train.getDelay() == 0) {
                System.out.println(train);
            } else {
                System.out.println(train + " | Delayed: " + train.getDelay() + " min");
            }
        }
    }

    // Print schedules for a station
    public void printStationSchedule(String stationName) {
        // This prints trains for the station in order of departure time,
        TreeMap<Integer, HybridTrain> stationMap = stationSchedules.get(stationName);
        if (stationMap == null || stationMap.isEmpty()) {
            System.out.println("No trains for station " + stationName);
            return;
        }
        for (HybridTrain train : stationMap.values()) {
            if (train.getDelay() == 0) {
                System.out.println(train);
            } else {
                System.out.println(train + " | Delayed: " + train.getDelay() + " min");
            }
        }
    }

    // Find next train globally (by earliest in scheduleByTime)
    public HybridTrain getNextTrain() {
        return earliestTrain;
    }
    // Find next train for a station
    public HybridTrain getNextTrain(String stationName) {
        TreeMap<Integer, HybridTrain> stationMap = stationSchedules.get(stationName);
        return (stationMap == null || stationMap.isEmpty()) ? null : stationMap.firstEntry().getValue();
    }

    // Delay a train and update the delay queue (still O(n) for remove, O(log n) for add)
    public void delayTrain(String trainID, int delayMinutes) {
        HybridTrain train = trainById.get(trainID);
        if (train == null) return;
        // Remove from delayQueue if already present (to update its priority)
        delayQueue.remove(train);
        train.setDelay(train.getDelay() + delayMinutes);
        delayQueue.add(train);

        // Calculate new departure time with wrap-around at 23:59
        int dep = train.getDepartureTime();
        int hours = dep / 100;
        int minutes = dep % 100;
        minutes += delayMinutes;
        hours += minutes / 60;
        minutes = minutes % 60;
        hours = hours % 24;
        int newDepartureTime = hours * 100 + minutes;

        rescheduleTrain(trainID, newDepartureTime, train.getStation());
    }

    // Print delayed trains by priority (highest delay first)
    public void printDelayedTrainsByPriority() {
        // This prints trains in order of delay (highest delay first, then by earliest departure time)
        PriorityQueue<HybridTrain> copy = new PriorityQueue<>(delayQueue);
        while (!copy.isEmpty()) {
            HybridTrain train = copy.poll();
            System.out.println(train + " | Delayed: " + train.getDelay() + " min");
        }
    }

    // Reschedule a train (now O(log n), does NOT touch delayQueue)
    public void rescheduleTrain(String trainID, int newDepartureTime, String newStation) {
        HybridTrain train = trainById.get(trainID);
        if (train == null) return;
        globalSchedule.remove(train.getDepartureTime());
        TreeMap<Integer, HybridTrain> oldStationMap = stationSchedules.get(train.getStation());
        if (oldStationMap != null) oldStationMap.remove(train.getDepartureTime());
        // Do NOT remove from delayQueue here
        train.setDepartureTime(newDepartureTime);
        train.setStation(newStation);
        addTrain(train);
        earliestTrain = globalSchedule.isEmpty() ? null : globalSchedule.firstEntry().getValue();
    }

    // Cancel a train (now O(log n), does NOT touch delayQueue)
    public void cancelTrain(String trainID) {
        HybridTrain train = trainById.remove(trainID);
        if (train == null) return;
        globalSchedule.remove(train.getDepartureTime());
        TreeMap<Integer, HybridTrain> stationMap = stationSchedules.get(train.getStation());
        if (stationMap != null) stationMap.remove(train.getDepartureTime());
        // Do NOT remove from delayQueue here
        earliestTrain = globalSchedule.isEmpty() ? null : globalSchedule.firstEntry().getValue();
    }
}