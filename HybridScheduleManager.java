import java.util.*;

public class HybridScheduleManager {
    // Fast lookup by train ID
    private final Map<String, HybridTrain> trainById = new HashMap<>();
    // Sorted by departure time
    private final TreeMap<Integer, List<HybridTrain>> scheduleByTime = new TreeMap<>();
    // Fast lookup by station name
    private final Map<String, HybridStation> stations = new HashMap<>();
    // Global next-train queue (by departure time)
    private final PriorityQueue<HybridTrain> globalPQ = new PriorityQueue<>(Comparator.comparingInt(HybridTrain::getDepartureTime));

    // Add a train schedule
    public void addTrain(HybridTrain train) {
        // Add to global PQ
        globalPQ.add(train);

        // Add to TreeMap for sorted printing
        scheduleByTime.computeIfAbsent(train.getDepartureTime(), k -> new ArrayList<>()).add(train);

        // Add to train ID map
        trainById.put(train.getTrainID(), train);

        // Add to station
        HybridStation station = stations.computeIfAbsent(train.getStation(), HybridStation::new);
        station.addTrain(train);
    }

    // Print all schedules (sorted by time)
    public void printAllSchedules() {
        for (var entry : scheduleByTime.entrySet()) {
            for (HybridTrain train : entry.getValue()) {
                System.out.println(train);
            }
        }
    }

    // Print schedules for a station
    public void printStationSchedule(String stationName) {
        HybridStation station = stations.get(stationName);
        if (station == null) {
            System.out.println("No trains for station " + stationName);
            return;
        }
        for (HybridTrain train : station.getTrainQueue()) {
            System.out.println(train);
        }
    }

    // Find next train globally
    public HybridTrain getNextTrain() {
        return globalPQ.peek();
    }

    // Find next train for a station
    public HybridTrain getNextTrain(String stationName) {
        HybridStation station = stations.get(stationName);
        return (station != null) ? station.getNextTrain() : null;
    }

    // Reschedule a train
    public void rescheduleTrain(String trainID, int newDepartureTime, String newStation) {
        HybridTrain train = trainById.get(trainID);
        if (train == null) return;

        // Remove from all structures
        globalPQ.remove(train);
        scheduleByTime.get(train.getDepartureTime()).remove(train);
        HybridStation oldStation = stations.get(train.getStation());
        if (oldStation != null) oldStation.removeTrain(train);

        // Update train
        train.setDepartureTime(newDepartureTime);
        train.setStation(newStation);

        // Re-insert
        addTrain(train);
    }

    // Delay a train
    public void delayTrain(String trainID, int delayMinutes) {
        HybridTrain train = trainById.get(trainID);
        if (train == null) return;
        rescheduleTrain(trainID, train.getDepartureTime() + delayMinutes, train.getStation());
    }

    // Cancel a train
    public void cancelTrain(String trainID) {
        HybridTrain train = trainById.remove(trainID);
        if (train == null) return;
        globalPQ.remove(train);
        scheduleByTime.get(train.getDepartureTime()).remove(train);
        HybridStation station = stations.get(train.getStation());
        if (station != null) station.removeTrain(train);
    }
}
