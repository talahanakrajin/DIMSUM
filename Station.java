import java.util.*;

public class Station {
    String name;
    PriorityQueue<Train> trainQueue = new PriorityQueue<>(Comparator.comparingInt(train -> train.priority));

    public Station(String name) {
        this.name = name;
    }

    public void addTrain(Train train) {
        if (train.priority <= 0) {
            return; // Ignore trains with non-positive priority
        }
        trainQueue.add(train);
    }

    public void cancelTrain(Train train) {
        if (trainQueue.contains(train)) {
            trainQueue.remove(train);
        } else {
            System.out.println("Train not found in the station's queue.");
        }
    }

    public void rescheduleTrain(Train train, int newPriority) {
        trainQueue.remove(train); // Remove the train from the queue
        train.priority = newPriority; // Update the priority
        trainQueue.add(train); // Re-add the train to the queue with updated priority
    }

    public Train getNextTrain() {
        return trainQueue.peek(); // Return the train with the highest priority
    }

    public void delayTrain(Train train, int delay) {
        trainQueue.remove(train);
        train.departureTime += delay; // Update the departure time

        Train closestTrain = null;
        int minTime = Integer.MAX_VALUE;

        for (Train t : trainQueue) {
            int timeDifference = Math.abs(t.departureTime - train.departureTime);
            if (timeDifference < minTime) {
                minTime = timeDifference;
                closestTrain = t;
            }
        }
        if (closestTrain != null) {
            train.priority += closestTrain.priority; // Update the priority based on the closest train's priority
        }

        trainQueue.add(train);
    }

    public PriorityQueue<Train> displayTrains() {
        return new PriorityQueue<>(trainQueue); // Return a copy of the train queue
        
    }
}
