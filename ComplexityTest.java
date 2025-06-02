public class ComplexityTest {
    public static void complexityTest(int testType, Train train, int n) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        switch (testType) {
            case 1:
                System.out.println("==== Running Test Type: Add schedules ====");
                int depTime = 500; // First departure time
                int interval = 10; // Interval in minutes
                for (int i = 0; i < n; i++) {
                    String trainID = String.format("TS%04d", i);
                    String station = train.getStationMap().get(1);
                    train.addToScheduleMap(depTime, trainID, station, true);
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
                break;
            case 2:
                System.out.println("==== Running Test Type: Print all schedules ====");
                train.printSchedule(null);
                break;
            case 3:
                System.out.println("==== Running Test Type: Print schedules for a specific station ====");
                train.printSchedule("Lebak Bulus");
                break;
            case 4:
                System.out.println("==== Running Test Type: Find the next train in the schedule ====");
                train.printNextTrain();
                break;
            case 5:
                System.out.println("==== Running Test Type: Reschedule a train ====");
                train.rescheduleTrain("TS0000", 500, 1120, train.getStationMap().get(5));
                break;
            case 6:
                System.out.println("==== Running Test Type: Delay a train ====");
                train.delayTrain("TS0012", 700, 10);
                break;
            case 7:
                System.out.println("==== Running Test Type: Cancel a train ====");
                train.cancelTrain("TS0006", 600);
                break;
            case 8:
                System.out.println("==== Running Test Type: Simulate trains running until a certain time ====");
                train.simulateTrainRunning(1200);
                System.out.println("Schedule AFTER simulation:");
                train.printSchedule(null);
                break;
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
        int n = 100; // Number of schedules to add (number of data)

        // Create the Train object
        Train train = new Train(null, 0);

        
        // DO ALL TESTING
        int totalCases = 7;
        for (int testType = 1; testType <= totalCases; testType++) {
            complexityTest(testType, train, n);
        }
        // complexityTest(2, train, n);
        
        
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

        /* 
        // INDIVIDUAL TESTING
        int testType = 11;
        complexityTest(1, train, n); // We have to add the schedules first
        complexityTest(testType, train, n);
        */


        // PROBLEMS: NOW THAT IF WE DELAY A TRAIN AND THE DEPARTURE TIME IS SIMILAR TO A PREVIOUS TRAIN ALREADY ADDED, THE OLD TRAIN DISSAPEAR
    }
}