package HashMap;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MRTSchedulePerformanceTest {
    private static Map<String, List<StationTime>> mrtSchedule = new HashMap<>();

    public static void main(String[] args) {
        int n = 100; 
        //initializeSchedule();
        
        // Run all test cases
        for (int testType = 1; testType <= 7; testType++) {
            complexityTest(testType, n);
        }
    }

    public static void complexityTest(int testType, int n) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        switch (testType) {
            case 1:
                System.out.println("==== Running Test Type: Add Train Schedules ====");
                for (int i = 0; i < n; i++) {
                    String trainName = "Train" + i;
                    List<StationTime> schedule = new ArrayList<>();
                    for (int j = 0; j < 12; j++) {
                        schedule.add(new StationTime("Station" + j, "10:00"));
                    }
                    mrtSchedule.put(trainName, schedule);
                }
                break;

            case 2:
                System.out.println("==== Running Test Type: View Full Schedule ====");
                viewFullSchedule();
                break;

            case 3:
                System.out.println("==== Running Test Type: View Station Schedule ====");
                viewStationSchedule("Bundaran HI");
                break;

            case 4:
                System.out.println("==== Running Test Type: Find Next Arriving Train ====");
                findNextArrivingTrain("Bundaran HI", "10:00");
                break;

            case 5:
                System.out.println("==== Running Test Type: Reschedule Train ====");
                rescheduleTrain("Train A", "Bundaran HI", "11:00");
                break;

            case 6:
                System.out.println("==== Running Test Type: Delay Train ====");
                delayTrain("Train A", 10);
                break;

            case 7:
                System.out.println("==== Running Test Type: Cancel Train ====");
                cancelTrain("Train0");
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

    private static void viewFullSchedule() {
        for (Map.Entry<String, List<StationTime>> entry : mrtSchedule.entrySet()) {
            for (StationTime st : entry.getValue()) {
            }
        }
    }

    private static void viewStationSchedule(String station) {
        for (Map.Entry<String, List<StationTime>> entry : mrtSchedule.entrySet()) {
            for (StationTime st : entry.getValue()) {
                if (st.station.equalsIgnoreCase(station)) {
                }
            }
        }
    }

    private static void findNextArrivingTrain(String station, String currentTimeStr) {
        LocalTime currentTime = LocalTime.parse(currentTimeStr, DateTimeFormatter.ofPattern("H:mm"));
        for (Map.Entry<String, List<StationTime>> entry : mrtSchedule.entrySet()) {
            for (StationTime st : entry.getValue()) {
                if (st.station.equalsIgnoreCase(station)) {
                    try {
                        LocalTime arrival = LocalTime.parse(st.time, DateTimeFormatter.ofPattern("H:mm"));
                        if (arrival.isAfter(currentTime)) {
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static void rescheduleTrain(String trainName, String station, String newTime) {
        List<StationTime> schedule = mrtSchedule.get(trainName);
        if (schedule != null) {
            for (StationTime st : schedule) {
                if (st.station.equalsIgnoreCase(station)) {
                    st.time = newTime;
                    break;
                }
            }
        }
    }

    private static void delayTrain(String trainName, int delay) {
        List<StationTime> schedule = mrtSchedule.get(trainName);
        if (schedule != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:mm");
            for (StationTime st : schedule) {
                try {
                    LocalTime t = LocalTime.parse(st.time, fmt);
                    t = t.plusMinutes(delay);
                    st.time = t.format(fmt);
                } catch (Exception ignored) {}
            }
        }
    }

    private static void cancelTrain(String trainName) {
        mrtSchedule.remove(trainName);
    }

    /* 
    private static void initializeSchedule() {
        mrtSchedule.put("Train A", new ArrayList<>(Arrays.asList(
            new StationTime("Bundaran HI", "5:00"),
            new StationTime("Dukuh Atas BNI", "5:05"),
            new StationTime("Setiabudi Astra", "5:10"),
            new StationTime("Bendungan Hilir", "5:15"),
            new StationTime("Istora", "5:20"),
            new StationTime("Senayan", "5:25"),
            new StationTime("ASEAN", "5:30"),
            new StationTime("Blok M", "5:35"),
            new StationTime("Blok A", "5:40"),
            new StationTime("Haji Nawi", "5:45"),
            new StationTime("Cipete Raya", "5:50"),
            new StationTime("Fatmawati", "5:55")
        )));
    }
    */
}

class StationTime {
    String station;
    String time;
    StationTime(String station, String time) {
        this.station = station;
        this.time = time;
    }
}