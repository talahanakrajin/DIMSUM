import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;
    private int maxTrainNumber = 0; // Track the highest train ID number (this is for the automatic train ID generation)

    TreeMap<Integer, List<String>> trainMap = new TreeMap<>(); // departure time: List of TrainIDs
    TreeMap<Integer, List<String>> currentStation = new TreeMap<>(); // departure time: List of StationNames
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

    public TreeMap<Integer, List<String>> getTrainMap() {
        return trainMap;
    }
    public TreeMap<Integer, List<String>> getCurrentStation() {
        return currentStation;
    }
    public int getMaxTrainNumber() {
        return maxTrainNumber;
    }

    // Add the delay in minutes to the HHMM time and return a new HHMM integer
    public int addMinutesToTime(int time, int minutesToAdd) {
        int hours = time / 100;
        int minutes = time % 100;
        minutes += minutesToAdd;
        hours += minutes / 60;
        minutes = minutes % 60;
        // Wrap around if hours >= 24
        hours = hours % 24;
        return (hours * 100) + minutes;
    }

    /* INPUT FUNCTIONS */
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

    // Add multiple schedules automatically for complexity testing or batch input
    public void addSchedulesAutomatically(String stationName, int startTime, int endTime, int headway) {
        int trainNumber = getMaxTrainNumber() + 1;
        int currentTime = startTime;
        while (currentTime <= endTime) {
            String autoTrainID = String.format("TS%04d", trainNumber);
            addToScheduleMap(currentTime, autoTrainID, stationName, false);
            currentTime = addMinutesToTime(currentTime, headway);
            trainNumber++;
        }
    }

    // Add a new train schedule
    public void addToScheduleMap(int departureTime, String trainID, String stationName, boolean testing) {
        trainMap.computeIfAbsent(departureTime, k -> new ArrayList<>()).add(trainID); // Add departure time and trainID to trainMap
        currentStation.computeIfAbsent(departureTime, k -> new ArrayList<>()).add(stationName); // Add departure time and stationName to currentStation (which is also a treemap)
        isOnTime.put(trainID, 0); // Declare the trainID as on time, since it is just added
        // Update maxTrainNumber if trainID in the format "TSxxxx"
        // This is only for automatic train ID generation from user input, not for complexity testing
        if (!testing && trainID != null && trainID.matches("TS\\d{4}")) {
            int num = Integer.parseInt(trainID.substring(2));
            if (num > maxTrainNumber) maxTrainNumber = num;
        }
    }

    // Print all or filtered by station
    public void printSchedule(String selectedStation) {
        boolean found = false;
        for (var entry : trainMap.entrySet()) {
            int depTime = entry.getKey();
            List<String> trainIDs = entry.getValue();
            List<String> stationNames = currentStation.getOrDefault(depTime, new ArrayList<>());
            int count = Math.max(trainIDs.size(), stationNames.size());
            for (int i = 0; i < count; i++) {
                String trainID = i < trainIDs.size() ? trainIDs.get(i) : "";
                String stationName = i < stationNames.size() ? stationNames.get(i) : "";
                int delay = isOnTime.getOrDefault(trainID, 0);
                if (selectedStation == null || stationName.equalsIgnoreCase(selectedStation)) {
                    String depTimeStr = String.format("%04d", depTime);
                    String hours = depTimeStr.substring(0, 2);
                    String minutes = depTimeStr.substring(2, 4);
                    System.out.printf("Train ID: %s | Departure Time: %s:%s | Station: %s", trainID, hours, minutes, stationName);
                    if (delay > 0) {
                        System.out.printf(" | delayed %d minutes", delay);
                    }
                    found = true;
                    System.out.println();
                }
            }
        }
        if (!found) {
            System.out.println("No trains found!");
        }
    }

    // Print the next departing train
    public void printNextTrain() {
        if (getTrainMap().isEmpty()) {
            System.out.println("No trains scheduled!");
            return;
        }
        int nextDepTime = getTrainMap().firstEntry().getKey(); // Get the first entry (earliest departure time)
        // Get the first train ID and station name for this departure time
        String nextTrainID = getTrainMap().firstEntry().getValue().get(0);
        String nextStationName = getCurrentStation().getOrDefault(nextDepTime, new java.util.ArrayList<>()).isEmpty()
            ? ""
            : getCurrentStation().get(nextDepTime).get(0);

        String nextDepTimeStr = String.format("%04d", nextDepTime);
        String hours = nextDepTimeStr.substring(0, 2);
        String minutes = nextDepTimeStr.substring(2, 4);

        int delay = isOnTime.getOrDefault(nextTrainID, 0);
        if (delay > 0) {
            System.out.printf("Next train departing:\nTrain ID: %s | Departure Time: %s:%s (Delayed by %d minutes) | Station: %s\n", nextTrainID, hours, minutes, delay, nextStationName);
        } else {
            System.out.printf("Next train departing:\nTrain ID: %s | Departure Time: %s:%s | Station: %s\n", nextTrainID, hours, minutes, nextStationName);
        }
    }

    // Reschedule a train
    public void rescheduleTrain(String trainID, int oldDepTime, int newDepTime, String newStation) {
        if (trainMap.containsKey(oldDepTime)) {
            if (!trainMap.get(oldDepTime).contains(trainID)) {
                System.out.println("Train ID " + trainID + " not found at the specified departure time.");
                return;
            }
            List<String> stationNames = currentStation.getOrDefault(oldDepTime, new ArrayList<>());
            String stationName = stationNames.isEmpty() ? "" : stationNames.get(0);
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);

            if (newStation == null) {
                System.out.println("No new station provided. Keeping the old station.");
            } else {
                stationName = newStation;
            }
            trainMap.put(newDepTime, new ArrayList<>(List.of(trainID)));
            currentStation.put(newDepTime, new ArrayList<>(List.of(stationName)));
            System.out.println("Train with ID: " + trainID + " rescheduled from: " + String.format("%04d", oldDepTime) + " to " + String.format("%04d", newDepTime));
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    // Delay a train
    public void delayTrain(String trainID, int oldDepTime, int delayMinutes) {
        if (trainMap.containsKey(oldDepTime)) {
            if (!trainMap.get(oldDepTime).contains(trainID)) {
                System.out.println("Train ID " + trainID + " not found at the specified departure time.");
                return;
            }
            List<String> stationNames = currentStation.getOrDefault(oldDepTime, new ArrayList<>());
            String stationName = stationNames.isEmpty() ? "" : stationNames.get(0);
            int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
            // Remove the train from the old departure time
            trainMap.get(oldDepTime).remove(trainID);
            currentStation.get(oldDepTime).remove(stationName);
            // Clean up if lists are empty
            if (trainMap.get(oldDepTime).isEmpty()) trainMap.remove(oldDepTime);
            if (currentStation.get(oldDepTime).isEmpty()) currentStation.remove(oldDepTime);
            // Add to the new departure time (append, don't overwrite)
            trainMap.computeIfAbsent(newDepTime, k -> new ArrayList<>()).add(trainID);
            currentStation.computeIfAbsent(newDepTime, k -> new ArrayList<>()).add(stationName);
            int currentDelay = isOnTime.getOrDefault(trainID, 0);
            isOnTime.put(trainID, currentDelay + delayMinutes);
            System.out.printf("Train %s delayed by %d minutes. New departure time: %04d\n", trainID, delayMinutes, newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    // Cancel a train
    public void cancelTrain(String trainID, int departureTime) {  
        if (trainMap.containsKey(departureTime) && trainMap.get(departureTime).contains(trainID)) {
            trainMap.remove(departureTime);
            currentStation.remove(departureTime);
            System.out.println("Train with ID: " + trainID + " at departure time: " + String.format("%04d", departureTime) + " has been cancelled.");
        } else {
            System.out.println("No train found with the specified departure time, or train ID does not match.");
        }
    }

    // Train running simulation (recursive, all-in-one)
    public void simulateTrainRunning(int closingTime) {
        if (currentStation.isEmpty()) {
            System.out.println("currentStation is empty.");
            return;
        }
        // Copy initial departures to avoid concurrent modification
        List<Integer> initialDepTimes = new ArrayList<>(currentStation.keySet());
        for (Integer depTime : initialDepTimes) {
            List<String> stationNames = currentStation.get(depTime);
            List<String> trainIDs = trainMap.get(depTime);
            if (stationNames == null || trainIDs == null) continue;
            int count = Math.min(stationNames.size(), trainIDs.size());
            for (int i = 0; i < count; i++) {
                String trainID = trainIDs.get(i);
                String stationName = stationNames.get(i);
                Integer stationKey = null;
                for (var stationEntry : getStationMap().entrySet()) {
                    if (stationEntry.getValue().equalsIgnoreCase(stationName)) {
                        stationKey = stationEntry.getKey();
                        break;
                    }
                }
                if (stationKey == null) continue;
                // Initial direction: forward if at 1, backward if at 13, otherwise forward
                boolean forward = (stationKey == 1) ? true : (stationKey == 13) ? false : true;
                simulateTrainRecursive(trainID, stationKey, depTime, forward, closingTime);
            }
        }
    }

    // Strict recursive simulation: move to next adjacent station, reverse only after reaching ends
    private void simulateTrainRecursive(String trainID, int stationKey, int currentTime, boolean forward, int closingTime) {
        // Base case: stop if time exceeds closingTime
        if (currentTime > closingTime) return;

        int nextKey;
        String travelKey;
        Integer travelTime;

        if (forward) {
            if (stationKey == 13) return; // At end, stop (do not reverse from same state)
            nextKey = stationKey + 1;
            travelKey = stationKey + "-" + nextKey;
            travelTime = getForwardTravelTimeMap().get(travelKey);
        } else {
            if (stationKey == 1) return; // At start, stop (do not reverse from same state)
            nextKey = stationKey - 1;
            travelKey = stationKey + "-" + nextKey;
            travelTime = getBackwardTravelTimeMap().get(travelKey);
        }

        if (travelTime == null) return;
        int nextTime = addMinutesToTime(currentTime, travelTime);
        if (nextTime > closingTime) return;

        String nextStationName = getStationMap().get(nextKey);

        // Prevent duplicate entries for the same train at the same time and station
        List<String> trainsAtTime = trainMap.computeIfAbsent(nextTime, k -> new ArrayList<>());
        List<String> stationsAtTime = currentStation.computeIfAbsent(nextTime, k -> new ArrayList<>());
        if (!(trainsAtTime.contains(trainID) && stationsAtTime.contains(nextStationName))) {
            trainsAtTime.add(trainID);
            stationsAtTime.add(nextStationName);
        }

        // Reverse direction only after moving to the end
        if (forward && nextKey == 13) {
            simulateTrainRecursive(trainID, nextKey, nextTime, false, closingTime);
        } else if (!forward && nextKey == 1) {
            simulateTrainRecursive(trainID, nextKey, nextTime, true, closingTime);
        } else {
            simulateTrainRecursive(trainID, nextKey, nextTime, forward, closingTime);
        }
    }
}
