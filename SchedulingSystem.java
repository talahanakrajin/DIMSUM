import java.util.*;

/**
 * Main scheduling system for trains.
 * Demonstrates use of Java Collections, exception handling, custom classes, and polymorphism.
 */
public class SchedulingSystem {
    // Fast lookup by referring to the trainID, <ID, Schedulable>
    private final Map<String, Schedulable> trainById = new HashMap<>();
    // Station name to CurrentStation mapping, <StationName, CurrentStation>
    private final Map<String, CurrentStation> stations = new HashMap<>();
    // Global schedule <departureTime, Schedulable>
    private final TreeMap<Integer, Schedulable> globalSchedule = new TreeMap<>();
    // Earliest train
    private Schedulable earliestTrain = null;
    // Delay queue for Schedulable
    private final PriorityQueue<Schedulable> delayQueue = new PriorityQueue<>(
        (a, b) -> {
            int cmp = Integer.compare(((Trains) b).getDelay(), ((Trains) a).getDelay());
            if (cmp == 0) {
                return Integer.compare(a.getDepartureTime(), b.getDepartureTime());
            }
            return cmp;
        }
    );
    private final String managerPassword = "eatdimsumeveryday";

    public boolean checkManagerPassword(String password) {
        return managerPassword.equals(password);
    }

    /**
     * Get a train by its ID.
     * @param id The train ID.
     * @return The Schedulable train object.
     */
    public Schedulable getTrainById(String id) {
        return trainById.get(id);
    }

    /**
     * Add a train to the schedule.
     * @param schedule The train to add.
     * @throws IllegalArgumentException if the departure time is invalid.
     */
    public void addTrain(Schedulable schedule) {
        // Always validate the departure time from the schedule object before adding
        int dep = schedule.getDepartureTime();
        TimeUtils.formatDepartureTime(dep); // Exception handling and validation
        globalSchedule.put(dep, schedule);
        trainById.put(((Trains) schedule).getTrainID(), schedule);
        stations.computeIfAbsent(((Trains) schedule).getCurrentStation(), k -> new CurrentStation(k)).addTrain((MRT) schedule);

        if (earliestTrain == null || dep < earliestTrain.getDepartureTime()) {
            earliestTrain = schedule;
        }
    }

    /** Print all train schedules. */
    public void printAllSchedules() {
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

    /** Print the schedule for a specific station. */
    public void printStationSchedule(String stationName) {
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

    /** @return the next train globally */
    public Schedulable getNextTrain() {
        return earliestTrain;
    }

    /** @return the next train at a specific station */
    public Schedulable getNextTrain(String stationName) {
        CurrentStation station = stations.get(stationName);
        return (station == null) ? null : station.getNextTrain();
    }

    /** Delay a train and update its schedule. */
    public void delayTrain(String trainID, int delayMinutes) {
        Schedulable schedule = trainById.get(trainID);
        if (schedule == null) return;
        Trains train = (Trains) schedule;
        delayQueue.remove(train);
        train.setDelay(train.getDelay() + delayMinutes);
        delayQueue.add(train);

        int dep = train.getDepartureTime();
        int newDepartureTime = TimeUtils.addMinutesHHMM(dep, delayMinutes);

        rescheduleTrain(trainID, newDepartureTime, train.getCurrentStation());
    }

    /** Print all delayed trains by priority. */
    public void printDelayedTrainsByPriority() {
        PriorityQueue<Schedulable> copy = new PriorityQueue<>(delayQueue);
        while (!copy.isEmpty()) {
            Trains train = (Trains) copy.poll();
            System.out.println(train + " | Delayed: " + train.getDelay() + " min");
        }
    }

    /** Reschedule a train to a new time and/or station. */
    public void rescheduleTrain(String trainID, int newDepartureTime, String newStation) {
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

    /** Cancel a train from the schedule. */
    public void cancelTrain(String trainID) {
        Schedulable schedule = trainById.remove(trainID);
        if (schedule == null) return;
        Trains train = (Trains) schedule;
        globalSchedule.remove(train.getDepartureTime());
        CurrentStation station = stations.get(train.getCurrentStation());
        if (station != null) station.removeTrain((MRT) train);
        earliestTrain = globalSchedule.isEmpty() ? null : globalSchedule.firstEntry().getValue();
    }
}