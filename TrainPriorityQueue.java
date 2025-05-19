import java.util.*;

class TrainPriorityQueue {
    List<Station> schedule = new ArrayList<>();
    PriorityQueue<Train> mainQueue = new PriorityQueue<>(Comparator.comparingInt(train -> train.priority));

    public void addStation(Station station) {
        schedule.add(station);
        mainQueue.addAll(station.trainQueue); // Add all trains from the station's queue to the main queue
    }

    public void viewSchedule(Station station) {
        for (Station st : schedule) {
            if (st.name.equals(station.name)) {
                System.out.println("Station: " + st.name);
                st.displayTrains(); // Display trains in the station's queue
                return;
            }
        }
        System.out.println("Station not found in the schedule.");
    }

    public Train getNextTrain(Station station) {
        for (Station st : schedule) {
            if (st.name.equals(station.name)) {
                return st.getNextTrain(); // Return the train with the highest priority in the station's queue
            }
        }
        System.out.println("Station not found in the schedule.");
        return null; // Return null if the station is not found
    }

    public void delayTrain(Station station, Train train, int delay) {
        for (Station st : schedule) {
            if (st.name.equals(station.name)) {
                st.delayTrain(train, delay); // Delay the train in the station's queue
                return;
            }
        }
        System.out.println("Station not found in the schedule.");
    }

    public void displayTrains() {
        for (Station station : schedule) {
            System.out.println("Station: " + station.name);
            station.displayTrains(); // Display trains in the station's queue
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

        public void displayTrains() {
            for (Train train : trainQueue) {
                System.out.println("Train ID: " + train.id + ", Priority: " + train.priority + ", Departure Time: " +
                        + train.departureTime);
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
        Train train4 = trainQueue.new Train(4, 2, 25);
        Train train5 = trainQueue.new Train(5, 1, 30);

        station1.addTrain(train1);
        station1.addTrain(train2);
        station1.addTrain(train4);
        station2.addTrain(train3);
        
        trainQueue.addStation(station1);
        trainQueue.addStation(station2);
        System.out.println("Trains in the queue:");

        trainQueue.viewSchedule(station1);

        trainQueue.displayTrains();

        trainQueue.delayTrain(station1, train1, 10);

        System.out.println("Trains in the queue after delay:");
        trainQueue.displayTrains();

        System.out.println("Next train to depart: " + trainQueue.getNextTrain(station2).id);
    }
}
