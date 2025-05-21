import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;

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

    // Add a new train schedule
    public void addToScheduleMap(int departureTime, String trainID, String stationName) {
        trainMap.computeIfAbsent(departureTime, k -> new ArrayList<>()).add(trainID); // Add departure time and trainID to trainMap
        currentStation.computeIfAbsent(departureTime, k -> new ArrayList<>()).add(stationName); // Add departure time and stationName to currentStation (which is also a treemap)
        isOnTime.put(trainID, 0); // Declare the trainID as on time, since it is just added
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

    /* Method overloading for rescheduling train */
    public void rescheduleTrain(int oldDepTime, int newDepTime, String newStation) {
        if (trainMap.containsKey(oldDepTime)) {
            List<String> trainIDs = trainMap.get(oldDepTime);
            String trainID = trainIDs.get(0);
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
            System.out.println("Train rescheduled from " + oldDepTime + " to " + newDepTime);
        } else {
            System.out.println("No train found with the specified departure time.");
        }
    }

    public void rescheduleTrain(String trainID, int newDepTime, String newStation) {
        Integer oldDepTime = null;
        String stationName = null;
        for (var entry : trainMap.entrySet()) {
            if (entry.getValue().contains(trainID)) {
                oldDepTime = entry.getKey();
                List<String> stationNames = currentStation.getOrDefault(oldDepTime, new ArrayList<>());
                stationName = stationNames.isEmpty() ? "" : stationNames.get(0);
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
            trainMap.put(newDepTime, new ArrayList<>(List.of(trainID)));
            currentStation.put(newDepTime, new ArrayList<>(List.of(stationName)));
        } else {
            System.out.println("No train found with the specified train ID.");
        }
    }

    public void delayTrain(String trainID, int delayMinutes) {
        Integer oldDepTime = null;
        String stationName = null;
        for (var entry : trainMap.entrySet()) {
            if (entry.getValue().contains(trainID)) {
                oldDepTime = entry.getKey();
                List<String> stationNames = currentStation.getOrDefault(oldDepTime, new ArrayList<>());
                stationName = stationNames.isEmpty() ? "" : stationNames.get(0);
                break;
            }
        }
        if (oldDepTime != null) {
            int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);
            trainMap.put(newDepTime, new ArrayList<>(List.of(trainID)));
            currentStation.put(newDepTime, new ArrayList<>(List.of(stationName)));
            int currentDelay = isOnTime.getOrDefault(trainID, 0);
            isOnTime.put(trainID, currentDelay + delayMinutes);
            System.out.printf("Train %s delayed by %d minutes. New departure time: %04d\n", trainID, delayMinutes, newDepTime);
        } else {
            System.out.println("No train found with the specified train ID.");
        }
    }

    public void delayTrain(int oldDepTime, int delayMinutes) {
        if (trainMap.containsKey(oldDepTime)) {
            List<String> trainIDs = trainMap.get(oldDepTime);
            String trainID = trainIDs.get(0);
            List<String> stationNames = currentStation.getOrDefault(oldDepTime, new ArrayList<>());
            String stationName = stationNames.isEmpty() ? "" : stationNames.get(0);
            int newDepTime = addMinutesToTime(oldDepTime, delayMinutes);
            trainMap.remove(oldDepTime);
            currentStation.remove(oldDepTime);
            trainMap.put(newDepTime, new ArrayList<>(List.of(trainID)));
            currentStation.put(newDepTime, new ArrayList<>(List.of(stationName)));
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
            if (entry.getValue().contains(trainID)) {
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

    // Train running simulation (Still on progress!)
    public void simulateTrainRunning(int closingTime) {
        if (currentStation.isEmpty()) {
            System.out.println("currentStation is empty.");
            return;
        }
        List<SimStep> stepsToAdd = new ArrayList<>();

        for (var entry : currentStation.entrySet()) {
            Integer depTime = entry.getKey();
            List<String> stationNames = entry.getValue();
            String trainID = trainMap.get(depTime).get(0);

            String stationName = stationNames.isEmpty() ? "" : stationNames.get(0);
            Integer stationKey = null;
            for (var stationEntry : getStationMap().entrySet()) {
                if (stationEntry.getValue().equalsIgnoreCase(stationName)) {
                    stationKey = stationEntry.getKey();
                    break;
                }
            }
            if (stationKey == null) {
                System.out.println("Invalid starting station for train " + trainID);
                continue;
            }

            boolean forward = (stationKey == 1); // Start forward if at 1, backward if at 13
            int currentKey = stationKey;
            int currentTime = depTime;

            while (currentTime <= closingTime) {
                int nextKey;
                String travelKey;
                Integer travelTime;

                if (forward) {
                    if (currentKey < 13) {
                        nextKey = currentKey + 1;
                        travelKey = currentKey + "-" + nextKey;
                        travelTime = getForwardTravelTimeMap().get(travelKey);
                    } else {
                        forward = false;
                        nextKey = currentKey - 1;
                        travelKey = currentKey + "-" + nextKey;
                        travelTime = getBackwardTravelTimeMap().get(travelKey);
                    }
                } else {
                    if (currentKey > 1) {
                        nextKey = currentKey - 1;
                        travelKey = currentKey + "-" + nextKey;
                        travelTime = getBackwardTravelTimeMap().get(travelKey);
                    } else {
                        forward = true;
                        nextKey = currentKey + 1;
                        travelKey = currentKey + "-" + nextKey;
                        travelTime = getForwardTravelTimeMap().get(travelKey);
                    }
                }

                if (travelTime == null) {
                    break;
                }
                int nextTime = addMinutesToTime(currentTime, travelTime);

                String nextStationName = getStationMap().get(nextKey);
                stepsToAdd.add(new SimStep(nextTime, trainID, nextStationName));

                currentTime = nextTime;
                currentKey = nextKey;

                if (currentTime > 2359 && currentKey == 1) {
                    break;
                }
            }
        }

        for (SimStep step : stepsToAdd) {
            trainMap.computeIfAbsent(step.time, k -> new ArrayList<>()).add(step.trainID);
            currentStation.computeIfAbsent(step.time, k -> new ArrayList<>()).add(step.stationName);
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

    // Helper class for simulation steps
    private static class SimStep {
        int time;
        String trainID;
        String stationName;
        SimStep(int time, String trainID, String stationName) {
            this.time = time;
            this.trainID = trainID;
            this.stationName = stationName;
        }
    }
}
