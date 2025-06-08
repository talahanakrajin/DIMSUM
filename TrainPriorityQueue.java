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
                
            }
            return;
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
        int n = 10; // Number of schedules to add
        TrainPriorityQueue trainQueue = new TrainPriorityQueue();

        //nt testType = 1; // Start with test type 1
        
        int totalCases = 7;
        for (int testType = 1; testType <= totalCases; testType++) {
        if (testType == 1) {
          // Always add schedules first for a fresh queue
        trainQueue = new TrainPriorityQueue();
          }
        int currentTestType = testType;
        TrainPriorityQueue currentQueue = trainQueue;
        Runnable testTask = () -> {
            switch (currentTestType) {
                case 1:
                    // Add schedules
                    for (int i = 0; i < n; i++) {
                        Station station = new Station("Station " + i);
                        for (int j = 0; j < 5; j++) {
                            Train train = new Train(j, (int) (Math.random() * 100) + 1,
                                    (int) (Math.random() * 2400));
                            station.addTrain(train);
                        }
                        currentQueue.addStation(station);
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
                    if (!currentQueue.schedule.isEmpty()) {
                        Station station = currentQueue.schedule.get(0);
                        station.getNextTrain();
 
                    }
                    break;
                case 5:
                    // Reschedule a train by ID
                    if (!currentQueue.schedule.isEmpty()) {
                        Station station = currentQueue.schedule.get(0);
                        Train train = station.trainQueue.peek();
                        if (train != null) {
                            station.rescheduleTrain(train, train.priority + 10);
                        }
                    }
                    break;
                case 6:
                    // Delay a train by ID
                    if (!currentQueue.schedule.isEmpty()) {
                        Station station = currentQueue.schedule.get(0);
                        Train train = station.trainQueue.peek();
                        if (train != null) {
                            station.delayTrain(train, 10);
                        }
                    }
                    break;
                
                case 7:
                    // Cancel a train by ID
                    if (!currentQueue.schedule.isEmpty()) {
                        Station station = currentQueue.schedule.get(0);
                        Train train = station.trainQueue.peek();
                        if (train != null) {
                            station.cancelTrain(train);
                        }
                    }
                    break;
               
            }
        };
        PerformanceMetrics.measureRuntime(testTask);
    }
}
}
