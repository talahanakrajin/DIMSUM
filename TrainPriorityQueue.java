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

    public void getNextTrain(Station station) {
        for (Station st : schedule) {
            if (st.name.equals(station.name)) {
                Train next = st.getNextTrain(); // Get the train with the highest priority
                if (next != null) {
                    System.out.println("Next train at " + st.name + ": ID=" + next.id + ", Priority=" + next.priority
                            + ", Departure Time=" + next.departureTime);
                } else {
                    System.out.println("No trains available at " + st.name);
                }
                return;
            }
        }
        System.out.println("Station not found in the schedule.");
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

    // Add this function to the main class
    public void cancelFunction() {
        for (Station station : schedule) {
            Train train = station.trainQueue.peek();
            if (train != null) {
                station.cancelTrain(train);
            }
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

        public void rescheduleTrain(Train train, int newDepartureTime) {
            if (trainQueue.contains(train)) {
                trainQueue.remove(train);
            } else {
                System.out.println("Train not found in the station's queue.");
                return;
            }
            if (newDepartureTime < 0) {
                System.out.println("Invalid departure time.");
                return;
            }
            train.departureTime = newDepartureTime;
            trainQueue.add(train);
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
                        +train.departureTime);
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
        PerformanceMetrics pm = new PerformanceMetrics();

        Random random = new Random();

        Station station1 = trainQueue.new Station("Station 1");
        
        Runnable task0 = () -> {
            for (int i = 0; i < 10; i++) {
                Train train = trainQueue.new Train(i, random.nextInt(100), random.nextInt(100));
                station1.addTrain(train);
            }
        };
        
        // Prepare stations and trains
        /*for (int i = 0; i < 10000; i++) {
            Station station = trainQueue.new Station("Station " + i);
            for (int j = 0; j < 5; j++) {
                Train train = trainQueue.new Train(j, random.nextInt(100), random.nextInt(100));
                station.addTrain(train);
            }
            trainQueue.addStation(station);
        }*/

        // Test delayTrain
        /*Runnable task1 = () -> {
            for (int i = 0; i < 10000; i++) {
                trainQueue.delayTrain(trainQueue.schedule.get(i), trainQueue.schedule.get(i).trainQueue.peek(),
                        random.nextInt(20));
            }
        };*/

        // Test addStation
        /*Runnable task2 = () -> {
            for (int i = 10000; i < 11000; i++) {
                Station station = trainQueue.new Station("Station " + i);
                for (int j = 0; j < 5; j++) {
                    Train train = trainQueue.new Train(j, random.nextInt(100), random.nextInt(100));
                    station.addTrain(train);
                }
                trainQueue.addStation(station);
            }
        };*/

        // Test viewSchedule
        /*Runnable task3 = () -> {
            for (int i = 0; i < 100; i++) {
                trainQueue.viewSchedule(trainQueue.schedule.get(i));
            }
        };*/

        // Test getNextTrain
        /*Runnable task4 = () -> {
            for (int i = 0; i < 100; i++) {
                trainQueue.getNextTrain(trainQueue.schedule.get(i));
            }
        };*/

        // Test displayTrains
        /*Runnable task5 = () -> {
            trainQueue.displayTrains();
        };*/

        // Test cancelTrain via cancelFunction
        /*Runnable task6 = () -> {
            for (Station station : new ArrayList<>(trainQueue.schedule)) {
                Train train = station.trainQueue.peek();
                if (train != null) {
                    station.cancelTrain(train);
                }
            }
        };*/


        System.out.println("Measuring performance metrics...");
        pm.measureRuntime(task0);
        
    }

}
