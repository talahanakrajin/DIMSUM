import java.util.*;

class TrainPriorityQueue {
    List<Station> schedule = new ArrayList<>();

    public void addStation(Station station) {
        schedule.add(station);
    }

    public void viewSchedule(Station station) {
        for (Station st : schedule) {
            if (st.name.equals(station.name)) {
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

    public List<Station> displayTrains() {
        for (Station station : schedule) {
            station.displayTrains(); // Display trains in the station's queue
        }
        return schedule; // Return the list of stations with their trains
    }

    public static void main(String[] args) {
        int n = 10000; // Number of schedules to add
        TrainPriorityQueue trainQueue = new TrainPriorityQueue();

        int totalCases = 7;
        int repeat = 5; // Number of times to repeat all tests
        for (int r = 1; r <= repeat; r++) {
            System.out.println("=== Test Run #" + r + " ===");
            for (int testType = 1; testType <= totalCases; testType++) {
                System.out.println("Running test case: " + testType);
                if (testType == 1) {
                    // Always add schedules first for a fresh queue
                    trainQueue = new TrainPriorityQueue();
                }
                int currentTestType = testType;
                TrainPriorityQueue currentQueue = trainQueue;

                // The start of the testing function
                Runnable testTask = () -> {
                    Station station = null;
                    Train train = null;
                    if (!currentQueue.schedule.isEmpty()) {
                        station = currentQueue.schedule.get(0);
                        if (!station.trainQueue.isEmpty()) {
                            train = station.trainQueue.peek();
                        }
                    }
                    switch (currentTestType) {
                        case 1:
                            // Add schedules
                            for (int i = 0; i < n; i++) {
                                Station station1 = new Station("Station " + i);
                                for (int j = 0; j < 5; j++) {
                                    Train train1 = new Train(j, (int) (Math.random() * 100) + 1,
                                            (int) (Math.random() * 2400));
                                    station1.addTrain(train1);
                                }
                                currentQueue.addStation(station1);
                            }
                            break;
                        case 2:
                            // Print all schedules
                            currentQueue.displayTrains();
                            break;
                        case 3:
                            // Print schedules for a specific station
                            if (!currentQueue.schedule.isEmpty()) {
                                currentQueue.viewSchedule(currentQueue.schedule.get(0));
                            }
                            break;
                        case 4:
                            // Find the next train in the schedule
                            if (station != null) {
                                currentQueue.getNextTrain(station);
                            }
                            break;
                        case 5:
                            // Reschedule a train by ID
                            if (station != null && train != null) {
                                station.rescheduleTrain(train, train.priority + 10);
                            }
                            break;
                        case 6:
                            // Delay a train by ID
                            if (station != null && train != null) {
                                station.delayTrain(train, 10);
                            }
                            break;
                        case 7:
                            // Cancel a train by ID
                            if (station != null && train != null) {
                                station.cancelTrain(train);
                            }
                            break;
                    }
                };
                PerformanceMetrics.measureRuntime(testTask);
            }
        }
    }
}

