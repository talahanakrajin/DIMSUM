package HashMap;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MRTScheduleManager {
    private static Map<String, List<StationTime>> mrtSchedule = new HashMap<>();
    private static boolean isRunning = true;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSchedule();
        System.out.println("Select role: 1. Passenger  2. Manager");
        int role = scanner.nextInt();
        scanner.nextLine();
        if (role == 1) {
            passengerMenu();
        } else if (role == 2) {
            managerMenu();
        } else {
            System.out.println("Invalid role. Exiting.");
        }
        scanner.close();
    }

    private static void passengerMenu() {
        isRunning = true;
        while (isRunning) {
            System.out.println("\n=== Passenger Menu ===");
            System.out.println("1. View Full Schedule");
            System.out.println("2. View Each Station's Schedule");
            System.out.println("3. Find Next Arriving Train");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewFullSchedule();
                    break;
                case 2:
                    viewStationSchedule();
                    break;
                case 3:
                    findNextArrivingTrain();
                    break;
                case 4:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void managerMenu() {
        isRunning = true;
        while (isRunning) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. View Full Schedule");
            System.out.println("2. View Each Station's Schedule");
            System.out.println("3. Add Train Schedule");
            System.out.println("4. Reschedule Train");
            System.out.println("5. Delay Train");
            System.out.println("6. Cancel Train/Stop");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewFullSchedule();
                    break;
                case 2:
                    viewStationSchedule();
                    break;
                case 3:
                    addTrainSchedule();
                    break;
                case 4:
                    rescheduleTrain();
                    break;
                case 5:
                    delayTrain();
                    break;
                case 6:
                    cancelTrainOrStop();
                    break;
                case 7:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewFullSchedule() {
        System.out.println("\n=== Full MRT Schedule ===");
        for (Map.Entry<String, List<StationTime>> entry : mrtSchedule.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (StationTime st : entry.getValue()) {
                System.out.println("  " + st.station + " at " + st.time);
            }
        }
    }

    private static void viewStationSchedule() {
        System.out.print("Enter station name: ");
        String station = scanner.nextLine();
        boolean found = false;
        for (Map.Entry<String, List<StationTime>> entry : mrtSchedule.entrySet()) {
            String trainId = entry.getKey();
            for (StationTime st : entry.getValue()) {
                if (st.station.equalsIgnoreCase(station)) {
                    if (!found) {
                        System.out.println("\nSchedule for " + station + ":");
                        found = true;
                    }
                    System.out.println(trainId + " arrives at " + st.time);
                }
            }
        }
        if (!found) {
            System.out.println("No schedule found for " + station);
        }
    }

    private static void findNextArrivingTrain() {
        System.out.print("Enter station name: ");
        String station = scanner.nextLine();
        System.out.print("Enter current time (HH:mm): ");
        String currentTimeStr = scanner.nextLine();
        LocalTime currentTime;
        try {
            currentTime = LocalTime.parse(currentTimeStr, DateTimeFormatter.ofPattern("H:mm"));
        } catch (Exception e) {
            System.out.println("Invalid time format.");
            return;
        }
        String nextTrain = null;
        String nextTime = null;
        LocalTime soonest = null;
        for (Map.Entry<String, List<StationTime>> entry : mrtSchedule.entrySet()) {
            String trainId = entry.getKey();
            for (StationTime st : entry.getValue()) {
                if (st.station.equalsIgnoreCase(station)) {
                    try {
                        LocalTime arrival = LocalTime.parse(st.time, DateTimeFormatter.ofPattern("H:mm"));
                        if ((soonest == null || arrival.isBefore(soonest)) && arrival.isAfter(currentTime)) {
                            soonest = arrival;
                            nextTrain = trainId;
                            nextTime = st.time;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        if (nextTrain != null) {
            System.out.println("Next train at " + station + ": " + nextTrain + " arrives at " + nextTime);
        } else {
            System.out.println("No upcoming trains for " + station + ".");
        }
    }

    private static void addTrainSchedule() {
        System.out.print("Enter new train name: ");
        String trainName = scanner.nextLine();
        List<StationTime> schedule = new ArrayList<>();
        System.out.println("Enter station and time pairs (type 'done' to finish):");
        while (true) {
            System.out.print("Station: ");
            String station = scanner.nextLine();
            if (station.equalsIgnoreCase("done")) break;
            System.out.print("Time (HH:mm): ");
            String time = scanner.nextLine();
            schedule.add(new StationTime(station, time));
        }
        mrtSchedule.put(trainName, schedule);
        System.out.println("Train schedule for " + trainName + " added.");
    }

    private static void rescheduleTrain() {
        System.out.print("Enter train name: ");
        String trainName = scanner.nextLine();
        List<StationTime> schedule = mrtSchedule.get(trainName);
        if (schedule == null) {
            System.out.println("Train not found.");
            return;
        }
        System.out.print("Enter station to reschedule: ");
        String station = scanner.nextLine();
        boolean found = false;
        for (StationTime st : schedule) {
            if (st.station.equalsIgnoreCase(station)) {
                System.out.print("Current time is " + st.time + ". Enter new time (HH:mm): ");
                String newTime = scanner.nextLine();
                st.time = newTime;
                found = true;
                System.out.println("Rescheduled " + trainName + " at " + station + " to " + newTime);
                break;
            }
        }
        if (!found) {
            System.out.println("Station not found in this train's schedule.");
        }
    }

    private static void delayTrain() {
        System.out.print("Enter train name to delay: ");
        String trainName = scanner.nextLine();
        List<StationTime> schedule = mrtSchedule.get(trainName);
        if (schedule == null) {
            System.out.println("Train not found.");
            return;
        }
        System.out.print("Enter delay in minutes: ");
        int delay = scanner.nextInt();
        scanner.nextLine();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:mm");
        for (StationTime st : schedule) {
            try {
                LocalTime t = LocalTime.parse(st.time, fmt);
                t = t.plusMinutes(delay);
                st.time = t.format(fmt);
            } catch (Exception ignored) {}
        }
        System.out.println("Delayed all stops for " + trainName + " by " + delay + " minutes.");
    }

    private static void cancelTrainOrStop() {
        System.out.println("1. Cancel entire train\n2. Cancel train at a specific station");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            System.out.print("Enter train name to cancel: ");
            String trainName = scanner.nextLine();
            if (mrtSchedule.remove(trainName) != null) {
                System.out.println("Train " + trainName + " cancelled.");
            } else {
                System.out.println("Train not found.");
            }
        } else if (choice == 2) {
            System.out.print("Enter train name: ");
            String trainName = scanner.nextLine();
            List<StationTime> schedule = mrtSchedule.get(trainName);
            if (schedule == null) {
                System.out.println("Train not found.");
                return;
            }
            System.out.print("Enter station to remove from train's schedule: ");
            String station = scanner.nextLine();
            boolean removed = schedule.removeIf(st -> st.station.equalsIgnoreCase(station));
            if (removed) {
                System.out.println("Removed stop at " + station + " for train " + trainName);
            } else {
                System.out.println("Station not found in this train's schedule.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

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
        mrtSchedule.put("Train B", new ArrayList<>(Arrays.asList(
            new StationTime("Bundaran HI", "13:00"),
            new StationTime("Dukuh Atas BNI", "13:05"),
            new StationTime("Setiabudi Astra", "13:10"),
            new StationTime("Bendungan Hilir", "13:15"),
            new StationTime("Istora", "13:20"),
            new StationTime("Senayan", "13:25"),
            new StationTime("ASEAN", "13:30"),
            new StationTime("Blok M", "13:35"),
            new StationTime("Blok A", "13:40"),
            new StationTime("Haji Nawi", "13:45"),
            new StationTime("Cipete Raya", "13:50"),
            new StationTime("Fatmawati", "13:55")
        )));
        mrtSchedule.put("Train C", new ArrayList<>(Arrays.asList(
            new StationTime("Bundaran HI", "17:00"),
            new StationTime("Dukuh Atas BNI", "17:05"),
            new StationTime("Setiabudi Astra", "17:10"),
            new StationTime("Bendungan Hilir", "17:15"),
            new StationTime("Istora", "17:20"),
            new StationTime("Senayan", "17:25"),
            new StationTime("ASEAN", "17:30"),
            new StationTime("Blok M", "17:35"),
            new StationTime("Blok A", "17:40"),
            new StationTime("Haji Nawi", "17:45"),
            new StationTime("Cipete Raya", "17:50"),
            new StationTime("Fatmawati", "17:55")
        )));
    }
}

class StationTime {
    String station;
    String time;
    StationTime(String station, String time) {
        this.station = station;
        this.time = time;
    }
}

