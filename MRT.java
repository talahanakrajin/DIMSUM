import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.PriorityQueue;

/**
 * MRT is a concrete train type, extending Trains.
 * Demonstrates inheritance and polymorphism.
 */
public class MRT extends Trains {
    // Static data structures for all MRT trains and schedules
    private static final Map<String, Schedulable> trainById = new HashMap<>();
    private static final Map<String, CurrentStation> stations = new HashMap<>();
    private static final TreeMap<Integer, Schedulable> globalSchedule = new TreeMap<>();
    private static Schedulable earliestTrain = null;
    private static final PriorityQueue<Schedulable> delayQueue = new PriorityQueue<>(
        (a, b) -> {
            int cmp = Integer.compare(((Trains) b).getDelay(), ((Trains) a).getDelay());
            if (cmp == 0) {
                return Integer.compare(a.getDepartureTime(), b.getDepartureTime());
            }
            return cmp;
        }
    );

    /**
     * Constructor for MRT.
     * @param trainID Unique identifier for the train.
     * @param departureTime Departure time in HHMM format.
     * @param currentStation Name of the current station.
     */
    public MRT(String trainID, int departureTime, String currentStation) {
        super(trainID, departureTime, currentStation);
    }

    /** MRT-specific operation (polymorphism) */
    @Override
    public void operate() {
        // MRT-specific operation logic (placeholder)
    }

    public static Schedulable getTrainById(String id) {
        return trainById.get(id);
    }

    public static void addTrain(Schedulable schedule) {
        int dep = schedule.getDepartureTime();
        TimeUtils.formatDepartureTime(dep); // Exception handling and validation
        globalSchedule.put(dep, schedule);
        trainById.put(((Trains) schedule).getTrainID(), schedule);
        stations.computeIfAbsent(((Trains) schedule).getCurrentStation(), k -> new CurrentStation(k)).addTrain((MRT) schedule);

        if (earliestTrain == null || dep < earliestTrain.getDepartureTime()) {
            earliestTrain = schedule;
        }
    }

    public static void printAllSchedules() {
        for (var entry : globalSchedule.entrySet()) {
            Schedulable schedule = entry.getValue();
            Trains train = (Trains) schedule;
            if (train.getDelay() == 0) {
                System.out.println(train);
            } else {
                System.out.println(train + " | Delayed: " + train.getDelay() + " min");
            }
        }
    }

    public static void printStationSchedule(String stationName) {
        CurrentStation station = stations.get(stationName);
        if (station == null || station.getSchedule().isEmpty()) {
            System.out.println("No trains for station " + stationName);
            return;
        }
        for (MRT train : station.getSchedule().values()) {
            if (train.getDelay() == 0) {
                System.out.println(train);
            } else {
                System.out.println(train + " | Delayed: " + train.getDelay() + " min");
            }
        }
    }

    public static Schedulable getNextTrain() {
        return earliestTrain;
    }

    public static Schedulable getNextTrain(String stationName) {
        CurrentStation station = stations.get(stationName);
        return (station == null) ? null : station.getNextTrain();
    }

    public static void delayTrain(String trainID, int delayMinutes) {
        Schedulable schedule = trainById.get(trainID);
        if (schedule == null) return;
        Trains train = (Trains) schedule;
        delayQueue.remove(train);
        train.setDelay(train.getDelay() + delayMinutes);
        delayQueue.add(train);

        int dep = train.getDepartureTime();
        int newDepartureTime = TimeUtils.addMinutesToDepTime(dep, delayMinutes);

        rescheduleTrain(trainID, newDepartureTime, train.getCurrentStation());
    }

    public static void printDelayedTrainsByPriority() {
        PriorityQueue<Schedulable> copy = new PriorityQueue<>(delayQueue);
        while (!copy.isEmpty()) {
            Trains train = (Trains) copy.poll();
            System.out.println(train + " | Delayed: " + train.getDelay() + " min");
        }
    }

    public static void rescheduleTrain(String trainID, int newDepartureTime, String newStation) {
        Schedulable schedule = trainById.get(trainID);
        if (schedule == null) return;
        Trains train = (Trains) schedule;
        globalSchedule.remove(train.getDepartureTime());
        CurrentStation oldStation = stations.get(train.getCurrentStation());
        if (oldStation != null) oldStation.removeTrain((MRT) train);
        train.setDepartureTime(newDepartureTime);
        train.setCurrentStation(newStation);
        addTrain((MRT) train);
        earliestTrain = globalSchedule.isEmpty() ? null : globalSchedule.firstEntry().getValue();
    }

    public static void cancelTrain(String trainID) {
        Schedulable schedule = trainById.remove(trainID);
        if (schedule == null) return;
        Trains train = (Trains) schedule;
        globalSchedule.remove(train.getDepartureTime());
        CurrentStation station = stations.get(train.getCurrentStation());
        if (station != null) station.removeTrain((MRT) train);
        earliestTrain = globalSchedule.isEmpty() ? null : globalSchedule.firstEntry().getValue();
    }
}

