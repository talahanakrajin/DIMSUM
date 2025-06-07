import java.util.ArrayList;
import java.util.List;

public class ComplexityTest {
    public static void complexityTest(int testType, Train train, int n, int idx) {
        // Precompute train schedule data
        List<String> trainIDs = new ArrayList<>(n);
        List<String> stations = new ArrayList<>(n);
        List<Integer> depTimes = new ArrayList<>(n);
        int depTime = 500; // First departure time
        int interval = 10; // Interval in minutes
        for (int i = 0; i < n; i++) {
            trainIDs.add(String.format("TS%04d", i));
            stations.add(train.getStationMap().get(1));
            depTimes.add(depTime);
            int hour = depTime / 100;
            int minute = depTime % 100;
            minute += interval;
            if (minute >= 60) {
                hour++;
                minute -= 60;
            }
            if (hour > 23) {
                hour = 0;
            }
            depTime = hour * 100 + minute;
            if (depTime > 2359) {
                depTime = 0;
            }
        }

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        switch (testType) {
            case 1 -> {
                System.out.println("==== Running Test Type: Add schedules ====");
                for (int i = 0; i < n; i++) {
                    train.addToScheduleMap(depTimes.get(i), trainIDs.get(i), stations.get(i), true);
                }
            }
            case 2 -> {
                System.out.println("==== Running Test Type: Print all schedules ====");
                train.printSchedule(null);
            }
            case 3 -> {
                System.out.println("==== Running Test Type: Print schedules for a specific station ====");
                train.printSchedule("Lebak Bulus");
            }
            case 4 -> {
                System.out.println("==== Running Test Type: Find the next train in the schedule ====");
                train.printNextTrain();
            }
            case 5 -> {
                System.out.println("==== Running Test Type: Reschedule a train ====");
                switch (idx) {
                    case 0 -> train.rescheduleTrain("TS0000", 500, 510, "Fatmawati");
                    case 1 -> train.rescheduleTrain("TS0001", 510, 520, "Cipete Raya");
                    case 2 -> train.rescheduleTrain("TS0002", 520, 530, "Haji Nawi");
                    case 3 -> train.rescheduleTrain("TS0003", 530, 540, "Blok A");
                    case 4 -> train.rescheduleTrain("TS0004", 540, 550, "Blok M");
                }
            }
            case 6 -> {
                System.out.println("==== Running Test Type: Delay a train ====");
                switch (idx) {
                    case 0 -> train.delayTrain("TS0000", 510, 5);
                    case 1 -> train.delayTrain("TS0001", 520, 15);
                    case 2 -> train.delayTrain("TS0002", 530, 20);
                    case 3 -> train.delayTrain("TS0003", 540, 30);
                    case 4 -> train.delayTrain("TS0004", 550, 25);
                }
            }
            case 7 -> {
                System.out.println("==== Running Test Type: Cancel a train ====");
                switch (idx) {
                    case 0 -> train.cancelTrain("TS0010", 640);
                    case 1 -> train.cancelTrain("TS0011", 650);
                    case 2 -> train.cancelTrain("TS0012", 700);
                    case 3 -> train.cancelTrain("TS0020", 820);
                    case 4 -> train.cancelTrain("TS0021", 830);
                }
            }
            case 8 -> {
                System.out.println("==== Running Test Type: Simulate trains running until a certain time ====");
                train.simulateTrainRunning(1200);
            }
        }
        long endTime = System.nanoTime();
        runtime.gc();
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        double elapsedMs = (endTime - startTime) / 1000000.0;
        double usedMemKB = (afterUsedMem - beforeUsedMem) / (1024.0);
        System.out.println("Time: " + elapsedMs + " ms");
        System.out.println("Memory: " + usedMemKB + " KB\n");
    }

    public static void main(String[] args) {
        /*
        LIST OF TEST TYPES:
        1. Add schedules
        2. Print all schedules
        3. Print schedules for a specific station
        4. Find the next train in the schedule
        5. Reschedule a train
        6. Delay a train 
        7. Cancel a train 
        8. Simulate trains running until a certain time
        */

        int n = 10000; // Number of schedules to add (number of data)

        // Create the Train object
        Train train = new Train(null, 0);

        // INDIVIDUAL TESTING
        int repetitions = 5; // Number of repetitions for each test type

        // Add schedules first
        complexityTest(1, train, n, 0);
        // Before we print schedules, let's change some train schedules to Bundaran HI
        train.rescheduleTrain("TS0000", 500, 500, "Bundaran HI");
        train.rescheduleTrain("TS0001", 510, 510, "Bundaran HI");
        train.rescheduleTrain("TS0002", 520, 520, "Bundaran HI");
        train.rescheduleTrain("TS0003", 530, 530, "Bundaran HI");
        train.rescheduleTrain("TS0004", 540, 540, "Bundaran HI");
        train.rescheduleTrain("TS0005", 550, 550, "Bundaran HI");
        train.rescheduleTrain("TS0006", 600, 600, "Bundaran HI");
        train.rescheduleTrain("TS0007", 610, 610, "Bundaran HI");

        // Print all schedules
        for (int idx = 0; idx < repetitions; idx++) {
            complexityTest(2, train, n, idx); // Print all schedules
        }
        // Print schedules for a specific station
        for (int idx = 0; idx < repetitions; idx++) {
            complexityTest(3, train, n, idx); // Print schedules for a specific station
        }
        // Find the next train in the schedule
        for (int idx = 0; idx < repetitions; idx++) {
            complexityTest(4, train, n, idx); // Print schedules for a specific station
        }
        // Reschedule a train
        for (int idx = 0; idx < repetitions; idx++) {
            complexityTest(5, train, n, idx); // Reschedule a train
        }
        // Delay a train
        for (int idx = 0; idx < repetitions; idx++) {
            complexityTest(6, train, n, idx); // Delay a train
        }
        // Cancel a train
        for (int idx = 0; idx < repetitions; idx++) {
            complexityTest(7, train, n, idx); // Cancel a train
        }
    }
}