/**
 * Utility class for train-related operations in the MRT system.
 * This class provides methods for:
 * - Validating and formatting train IDs
 * - Adding trains to the system
 * - Managing automatic train scheduling
 * Demonstrates use of static methods and utility functions.
 */
public final class TrainUtils {
    /** Private constructor to prevent instantiation */
    private TrainUtils() {}

    /**
     * Validates and formats a train ID to uppercase TSxxxx format.
     * @param trainID The input train ID to validate
     * @return The formatted train ID in uppercase
     * @throws IllegalArgumentException if the format is invalid
     */
    public static String formatTrainId(String trainID) {
        String upperID = trainID.toUpperCase();
        if (!upperID.matches("TS\\d{4}")) {
            throw new IllegalArgumentException("The ID format should be TSxxxx (x = positive integer, 'ts' will be converted to uppercase)");
        }
        return upperID;
    }

    /**
     * Prompts the user until a valid train ID is entered and returns it.
     * Continues prompting until a valid ID in TSxxxx format is provided.
     * @param sc Scanner for user input
     * @return Validated and formatted train ID
     */
    public static String promptValidTrainID(java.util.Scanner sc) {
        while (true) {
            System.out.print("Enter Train ID (TSxxxx): ");
            String id = sc.nextLine();
            try {
                return formatTrainId(id);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Utility for adding a single train to the system.
     * Creates a new MRT train with the specified parameters and adds it to the system.
     * @param system The scheduling system to add the train to
     * @param id The train ID
     * @param depTime The departure time in HHMM format
     * @param stationName The name of the station
     */
    public static void addTrain(SchedulingSystem system, String id, int depTime, String stationName) {
        boolean isNorthbound = StationUtils.checkInitialDirection(stationName);
        system.addTrain(new MRT(id, depTime, stationName, isNorthbound));
    }

    /**
     * Utility for adding multiple trains automatically to the system.
     * Creates trains at regular intervals between start and end times.
     * @param system The scheduling system to add trains to
     * @param stationName The name of the station
     * @param startTime The start time in HHMM format
     * @param endTime The end time in HHMM format
     * @param headway The time interval between trains in minutes
     */
    public static void autoAddTrain(SchedulingSystem system, String stationName, int startTime, int endTime, int headway) {
        int trainNumber = 1;
        int currentTime = startTime;
        boolean isNorthbound = StationUtils.checkInitialDirection(stationName);

        while (currentTime <= endTime) {
            String autoTrainID = String.format("TS%04d", trainNumber);
            system.addTrain(new MRT(autoTrainID, currentTime, stationName, isNorthbound));
            currentTime = TimeUtils.addMinutesToDepTime(currentTime, headway);
            trainNumber++;
        }
    }
}
