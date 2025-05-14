import java.util.*;

class TrainPriorityQueue {
    List<Station> schedule = new ArrayList<>();
    PriorityQueue<Train> mainQueue = new PriorityQueue<>(Comparator.comparingInt(train -> train.priority));

    public void addStation(Station station) {
        schedule.add(station);
        mainQueue.addAll(station.trainQueue); // Add all trains from the station's queue to the main queue
    }

    public void addTrain(Train train) {
        if (train.priority <= 0) {
            System.out.println("Train priority must be positive.");
            return;
        }
        mainQueue.add(train);
    }

    public Train getNextTrain() {
        return mainQueue.peek(); // Return the train with the highest priority
    }

    public void delayTrain(Train train, int delay) {
        if (!mainQueue.contains(train)) {
            System.out.println("Train not found in the main queue.");
            return;
        }
        mainQueue.remove(train);
        train.departureTime += delay; // Update the departure time

        Train closestTrain = null;
        int minTime = Integer.MAX_VALUE;

        for (Train t : mainQueue) {
            int timeDifference = Math.abs(t.departureTime - train.departureTime);
            if (timeDifference < minTime) {
                minTime = timeDifference;
                closestTrain = t;
            }
        }
        if (closestTrain != null) {
            train.priority += closestTrain.priority; // Update the priority based on the closest train's priority
        }

        mainQueue.add(train);
    }

    public void displayTrains() {
        for (Station st : schedule) {
            System.out.println("Station: " + st.name);
            st.displayTrains(); // Display trains in each station's queue
        }
        System.out.println("Main Queue:");
        for (Train train : mainQueue) {
            System.out.println("Train ID: " + train.id + ", Priority: " + train.priority + ", Depart in: "
                    + train.departureTime + " minutes");
        }
    }

    class Station {
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

        public void displayTrains() {
            for (Train train : trainQueue) {
                System.out.println("Train ID: " + train.id + ", Priority: " + train.priority + ", Depart in: "
                        + train.departureTime + " minutes");
            }
        }
    }

    class Train {
        int id;
        int priority;
        int departureTime;

        public Train(int id, int priority, int departureTime) {
            this.id = id;
            this.priority = priority;
            this.departureTime = departureTime;
        }
    }

    public static void main(String[] args) {
        TrainPriorityQueue trainQueue = new TrainPriorityQueue();

        Station station1 = trainQueue.new Station("Station 1");
        Station station2 = trainQueue.new Station("Station 2");

        Train train1 = trainQueue.new Train(1, 5, 10);
        Train train2 = trainQueue.new Train(2, 3, 20);
        Train train3 = trainQueue.new Train(3, 8, 15);

        station1.addTrain(train1);
        station1.addTrain(train2);
        station2.addTrain(train3);

        trainQueue.addStation(station1);
        trainQueue.addStation(station2);

        trainQueue.displayTrains();
 
        trainQueue.delayTrain(train1, 10);

        System.out.println("Trains in the queue after delay:");
        trainQueue.displayTrains();

        System.out.println("Next train to depart: " + trainQueue.getNextTrain().id);
    }
}
