/**
 * Utility class for time validation and calculation in the MRT system.
 * This class provides methods for:
 * - Validating time formats
 * - Converting between time formats
 * - Adding minutes to times with proper wrap-around
 * - Handling user input for time-related operations
 * Demonstrates use of static methods and exception handling.
 */
public final class TimeUtils {
    /** Private constructor to prevent instantiation */
    private TimeUtils() {}

    /**
     * Validates that a time is in HHMM format, between 0000 and 2359.
     * Checks both the overall range and the individual hour/minute components.
     * @param time The time to validate
     * @throws IllegalArgumentException if the time is invalid
     */
    public static void formatDepartureTime(int time) {
        if (time < 0 || time > 2359) {
            throw new IllegalArgumentException("Departure time must be between 0000 and 2359, got: " + String.format("%04d", time));
        }
        int hours = time / 100;
        int minutes = time % 100;
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Departure time must be in HHMM format (0000 to 2359), got: " + String.format("%04d", time));
        }
    }

    /**
     * Validates a time string input and converts it to an integer.
     * @param input The time string to validate
     * @return The validated time as an integer, or -1 if invalid
     */
    public static int checkValidTime(String input) {
        try {
            int dep = Integer.parseInt(input);
            formatDepartureTime(dep);
            return dep;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a numeric time in HHMM format.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Prompts the user for a valid time input.
     * Continues prompting until a valid time is entered.
     * @param sc Scanner for user input
     * @return A valid time in HHMM format
     */
    public static int promptValidTime(java.util.Scanner sc) {
        while (true) {
            System.out.print("Enter Time (Format = HHMM): ");
            String input = sc.nextLine();
            int dep = checkValidTime(input);
            if (dep != -1) return dep;
        }
    }

    /**
     * Prompts the user for a valid closing time input.
     * Continues prompting until a valid time is entered.
     * @param sc Scanner for user input
     * @return A valid closing time in HHMM format
     */
    public static int promptClosingTime(java.util.Scanner sc) {
        while (true) {
            System.out.print("Enter Closing Time (HHMM): ");
            String input = sc.nextLine();
            int dep = checkValidTime(input);
            if (dep != -1) return dep;
        }
    }

    /**
     * Prompts the user for a valid delay duration.
     * Continues prompting until a positive integer is entered.
     * @param sc Scanner for user input
     * @return A valid delay duration in minutes
     */
    public static int promptValidDelayMinutes(java.util.Scanner sc) {
        while (true) {
            System.out.print("Enter Delay Minutes: ");
            String input = sc.nextLine();
            try {
                int delay = Integer.parseInt(input);
                if (delay < 0) {
                    throw new IllegalArgumentException("Delay minutes must be a positive integer.");
                }
                return delay;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a positive integer for delay minutes.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Adds minutes to a time in HHMM format, with proper 24-hour wrap-around.
     * For example, adding 30 minutes to 2350 results in 0020.
     * @param depTime The original time in HHMM format
     * @param minutesToAdd The number of minutes to add
     * @return The new time in HHMM format
     */
    public static int addMinutesToDepTime(int depTime, int minutesToAdd) {
        // Convert HHMM to total minutes
        int totalMinutes = (depTime / 100) * 60 + (depTime % 100);
        
        // Add the delay minutes
        totalMinutes += minutesToAdd;
        
        // Handle 24-hour wrap-around
        totalMinutes = totalMinutes % (24 * 60);
        
        // Convert back to HHMM format
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        
        return hours * 100 + minutes;
    }
}
