import java.util.Scanner;
import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;

    TreeMap<Integer, String> scheduleMap = new TreeMap<>(); // departure time: "TrainID,StationName"
    TreeMap<String, Integer> isOnTime = new TreeMap<>(); // trainID: delayMinutes

    // Constructor
    public Train(String trainID, int departureTime) {
        this.trainID = trainID;
        this.departureTime = departureTime;
    }

    public String getTrainID() {
        return trainID;
    }
    public int getDepartureTime() {
        return departureTime;
    }
    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }
    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public TreeMap<Integer, String> getScheduleMap() {
        return scheduleMap;
    }

    public void addToScheduleMap(int departureTime, String trainID, String stationName) {
        scheduleMap.put(departureTime, trainID + "," + stationName);
        isOnTime.put(trainID, 0); // New train is on time by default
    }

    // Print all or filtered by station
    public void printSchedule(String selectedStation) {
        boolean found = false;
        for (var entry : scheduleMap.entrySet()) {
            int depTime = entry.getKey();
            String[] parts = entry.getValue().split(",", 2);
            String trainID = parts[0];
            String stationName = parts.length > 1 ? parts[1] : "";

            // Check if the train is delayed, if not it will default to 0
            int delay = isOnTime.getOrDefault(trainID, 0);
            if (selectedStation == null || stationName.equalsIgnoreCase(selectedStation)) {
                String depTimeStr = String.format("%04d", depTime);
                String hours = depTimeStr.substring(0, 2);
                String minutes = depTimeStr.substring(2, 4);
                System.out.printf("Train ID: %s | Departure Time: %s:%s | Station: %s", trainID, hours, minutes, stationName);
                if (delay > 0) {
                    System.out.printf(" | delayed %d minutes", delay);
                }
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No trains found!");
        }
    }

    // Input and validate train ID
    public void inputTrainID(Scanner sc) {
        String inputTrainID;
        while (true) {
            System.out.print("Enter the train ID with format: TSxxxx (Note that 'ts' will be converted to uppercase, and x = non-negative integer): ");
            inputTrainID = sc.nextLine().toUpperCase();
            if (inputTrainID.matches("TS[0-9]{4}")) {
                setTrainID(inputTrainID);
                break;
            } else {
                System.out.println("Invalid train ID! Enter with the format TSxxxx. (x = non-negative integer)");
            }
        }
    }
    // Input and validate departure time (returns the valid int)
    public int inputDepTime(Scanner sc) {
        int inputDepartureTime;
        while (true) {
            System.out.print("Enter the departure time (format: HHMM): ");
            if (sc.hasNextInt()) {
                inputDepartureTime = sc.nextInt();
                if (inputDepartureTime >= 0 && inputDepartureTime <= 2359 && inputDepartureTime % 100 < 60) {
                    sc.nextLine(); // consume newline
                    return inputDepartureTime;
                } else {
                    System.out.println("Invalid departure time! Enter between 0000-2359 and minutes must be 00-59.");
                }
            } else {
                System.out.println("Invalid input! Please enter a 4-digit number (HHMM).");
                sc.next(); // consume invalid input
            }
        }
    }
    // Prompts user to select a station number (0 for "keep original station" is allowed)
    public int inputStation(Scanner sc) {
        getStationMap().forEach((key, value) -> {
            System.out.println(key + ". " + value + "\n");
        });
        System.out.print("Enter the station number to reschedule (Enter 0 to keep the original station): ");

        int stationNum = -1;
        boolean validInput = false;
        while (!validInput) {
            if (sc.hasNextInt()) {
                stationNum = sc.nextInt();
                if (stationNum == 0 || getStationMap().containsKey(stationNum)) {
                    validInput = true;
                } else {
                    System.out.print("Invalid station number! Please enter a number between 0 - 13: ");
                }
            } else {
                System.out.print("Invalid input! Please enter a number between 0 - 13: ");
                sc.next();
            }
        }
        sc.nextLine(); // Consume the newline character
        return stationNum;
    }

    /* Method overloading for rescheduling train */
    public void rescheduleTrain(int oldDepTime, int newDepTime, String newStation) {
        if (scheduleMap.containsKey(oldDepTime)) {
            String value = scheduleMap.get(oldDepTime);
            String[] parts = value.split(",", 2);
            String trainID = parts[0];
            String stationName = parts.length > 1 ? parts[1] : "";
            scheduleMap.remove(oldDepTime);

            if (newStation == null) {
                System.out.println("No new station provided. Keeping the old station.");
            } else {
                stationName = newStation;
            }
            scheduleMap.put(newDepTime, trainID + "," + stationName);
            System.out.println("Train rescheduled from " + oldDepTime + " to " + newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    public void rescheduleTrain(String trainID, int newDepTime, String newStation) {
        for (var entry : scheduleMap.entrySet()) {
            String[] parts = entry.getValue().split(",", 2);
            if (parts[0].equalsIgnoreCase(trainID)) {
                int oldDepTime = entry.getKey();
                String stationName = parts.length > 1 ? parts[1] : "";
                scheduleMap.remove(oldDepTime);

                if (newStation == null) {
                    System.out.println("\nNo new station provided. Keeping the old station.");
                    System.out.print("Train Successfully rescheduled!\n");
                } else {
                    stationName = newStation;
                }
                scheduleMap.put(newDepTime, trainID + "," + stationName);
                return;
            }
        }
        System.out.println("No train found with the specified train ID.");
    }

    // Delay a train by Train ID and delay time (in minutes)
    public void delayTrain(String trainID, int delayMinutes) {
        boolean found = false;
        for (var entry : scheduleMap.entrySet()) {
            String[] parts = entry.getValue().split(",", 2);
            if (parts[0].equalsIgnoreCase(trainID)) {
                int oldDepTime = entry.getKey();
                String stationName = parts.length > 1 ? parts[1] : "";
                int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
                scheduleMap.remove(oldDepTime);
                scheduleMap.put(newDepTime, trainID + "," + stationName);
                int currentDelay = isOnTime.getOrDefault(trainID, 0);
                isOnTime.put(trainID, currentDelay + delayMinutes);
                System.out.printf("Train %s delayed by %d minutes. New departure time: %04d\n", trainID, delayMinutes, newDepTime);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No train found with the specified train ID.");
        }
    }

    // Delay a train by departure time (in HHMM)
    public void delayTrain(int oldDepTime, int delayMinutes) {
        if (scheduleMap.containsKey(oldDepTime)) {
            String value = scheduleMap.get(oldDepTime);
            String[] parts = value.split(",", 2);
            String trainID = parts[0];
            String stationName = parts.length > 1 ? parts[1] : "";
            int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
            scheduleMap.remove(oldDepTime);
            scheduleMap.put(newDepTime, trainID + "," + stationName);
            int currentDelay = isOnTime.getOrDefault(trainID, 0);
            isOnTime.put(trainID, currentDelay + delayMinutes);
            System.out.printf("Train %s delayed by %d minutes. New departure time: %04d\n", trainID, delayMinutes, newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    public void cancelTrain(String trainID) {
        boolean found = false;
        for (var entry : scheduleMap.entrySet()) {
            String[] parts = entry.getValue().split(",", 2);
            if (parts[0].equalsIgnoreCase(trainID)) {
                scheduleMap.remove(entry.getKey());
                System.out.println("Train " + trainID + " has been cancelled.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No train found with the specified train ID.");
        }
    }

    public void cancelTrain(int departureTime) {
        if (scheduleMap.containsKey(departureTime)) {
            scheduleMap.remove(departureTime);
            System.out.println("Train at " + departureTime + " has been cancelled.");
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    // Add the delay in minutes to the HHMM time and return a new HHMM integer
    private int addMinutesToTime(int time, int minutesToAdd) {
        int hours = time / 100;
        int minutes = time % 100;
        minutes += minutesToAdd;
        hours += minutes / 60;
        minutes = minutes % 60;
        // Wrap around if hours >= 24
        hours = hours % 24;
        return (hours * 100) + minutes;
    }
}
