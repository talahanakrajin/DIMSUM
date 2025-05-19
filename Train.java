import java.util.Scanner;
import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;

    TreeMap<Integer, String> trainMap = new TreeMap<>(); // departure time: TrainID
    TreeMap<Integer, String> currentStation = new TreeMap<>(); // departure time: StationName
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

    public TreeMap<Integer, String> getTrainMap() {
        return trainMap;
    }
    public TreeMap<Integer, String> getCurrentStation() {
        return currentStation;
    }

    // Add a new train schedule
    public void addToScheduleMap(int departureTime, String trainID, String stationName) {
        trainMap.put(departureTime, trainID);
        currentStation.put(departureTime, stationName);
        isOnTime.put(trainID, 0); // New train is on time by default
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

    // Print all or filtered by station
    public void printSchedule(String selectedStation) {
        boolean found = false;
        for (var entry : trainMap.entrySet()) {
            int depTime = entry.getKey();
            String trainID = entry.getValue();
            String stationName = currentStation.getOrDefault(depTime, "");
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
        if (trainMap.containsKey(oldDepTime)) {
            String trainID = trainMap.get(oldDepTime);
            String stationName = currentStation.getOrDefault(oldDepTime, "");
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);

            if (newStation == null) {
                System.out.println("No new station provided. Keeping the old station.");
            } else {
                stationName = newStation;
            }
            trainMap.put(newDepTime, trainID);
            currentStation.put(newDepTime, stationName);
            System.out.println("Train rescheduled from " + oldDepTime + " to " + newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    public void rescheduleTrain(String trainID, int newDepTime, String newStation) {
        Integer oldDepTime = null;
        String stationName = null;
        for (var entry : trainMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(trainID)) {
                oldDepTime = entry.getKey();
                stationName = currentStation.getOrDefault(oldDepTime, "");
                break;
            }
        }
        if (oldDepTime != null) {
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);

            if (newStation == null) {
                System.out.println("\nNo new station provided. Keeping the old station.");
                System.out.print("Train Successfully rescheduled!\n");
            } else {
                stationName = newStation;
            }
            trainMap.put(newDepTime, trainID);
            currentStation.put(newDepTime, stationName);
        } else {
            System.out.println("No train found with the specified train ID.");
        }
    }

    // Delay a train by Train ID and delay time (in minutes)
    public void delayTrain(String trainID, int delayMinutes) {
        Integer oldDepTime = null;
        String stationName = null;
        for (var entry : trainMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(trainID)) {
                oldDepTime = entry.getKey();
                stationName = currentStation.getOrDefault(oldDepTime, "");
                break;
            }
        }
        if (oldDepTime != null) {
            int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);
            trainMap.put(newDepTime, trainID);
            currentStation.put(newDepTime, stationName);
            int currentDelay = isOnTime.getOrDefault(trainID, 0);
            isOnTime.put(trainID, currentDelay + delayMinutes);
            System.out.printf("Train %s delayed by %d minutes. New departure time: %04d\n", trainID, delayMinutes, newDepTime);
        } else {
            System.out.println("No train found with the specified train ID.");
        }
    }

    // Delay a train by departure time (in HHMM)
    public void delayTrain(int oldDepTime, int delayMinutes) {
        if (trainMap.containsKey(oldDepTime)) {
            String trainID = trainMap.get(oldDepTime);
            String stationName = currentStation.getOrDefault(oldDepTime, "");
            int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);
            trainMap.put(newDepTime, trainID);
            currentStation.put(newDepTime, stationName);
            int currentDelay = isOnTime.getOrDefault(trainID, 0);
            isOnTime.put(trainID, currentDelay + delayMinutes);
            System.out.printf("Train %s delayed by %d minutes. New departure time: %04d\n", trainID, delayMinutes, newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    public void cancelTrain(String trainID) {
        Integer depTimeToRemove = null;
        for (var entry : trainMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(trainID)) {
                depTimeToRemove = entry.getKey();
                break;
            }
        }
        if (depTimeToRemove != null) {
            trainMap.remove(depTimeToRemove);
            currentStation.remove(depTimeToRemove);
            System.out.println("Train " + trainID + " has been cancelled.");
        } else {
            System.out.println("No train found with the specified train ID.");
        }
    }

    public void cancelTrain(int departureTime) {
        if (trainMap.containsKey(departureTime)) {
            trainMap.remove(departureTime);
            currentStation.remove(departureTime);
            System.out.println("Train at " + departureTime + " has been cancelled.");
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }
}
