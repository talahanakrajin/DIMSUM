/**
 * Utility class for time validation and calculation.
 * Demonstrates use of static methods and exception handling.
 */
public final class TimeUtils {
    private TimeUtils() {}

    /**
     * Validates that a time is in HHMM format, between 0000 and 2359.
     * @param time The time to validate.
     * @throws IllegalArgumentException if the time is invalid.
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
     * Prompts the user until a valid HHMM time is entered and returns it.
     * @param sc Scanner for user input.
     * @return Validated departure time in HHMM format.
     */
    public static int promptValidDepartureTime(java.util.Scanner sc) {
        while (true) {
            System.out.print("Enter Departure Time (HHMM): ");
            String input = sc.nextLine();
            try {
                int dep = Integer.parseInt(input);
                formatDepartureTime(dep);
                return dep;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a numeric time in HHMM format.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user until a valid positive integer is entered for delay minutes.
     * @param sc Scanner for user input.
     * @return Validated delay minutes (positive integer).
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
     * Adds minutes to a time in HHMM format, with wrap-around at 24 hours.
     * @param depTime The original time.
     * @param minutesToAdd Minutes to add.
     * @return The new time in HHMM format.
     */
    public static int addMinutesToDepTime(int depTime, int minutesToAdd) {
        int hours = depTime / 100;
        int minutes = depTime % 100;
        minutes += minutesToAdd;
        hours += minutes / 60;
        minutes = minutes % 60;
        hours = hours % 24;
        int result = hours * 100 + minutes;
        if (result < 0 || result > 2359) {
            throw new IllegalArgumentException("Resulting departure time must be between 0000 and 2359, got: " + String.format("%04d", result));
        }
        return result;
    }
}
