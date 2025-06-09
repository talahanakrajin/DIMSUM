/**
 * Utility class for time validation and calculation.
 * Demonstrates use of static methods and exception handling.
 */
public final class TimeUtils {
    private TimeUtils() {}

    /**
     * Validates that a time is in HHMM format.
     * @param time The time to validate.
     * @throws IllegalArgumentException if the time is invalid.
     */
    public static void validateHHMM(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Departure time must be in HHMM format (0000 to 2359), got: " + time);
        }
    }

    /**
     * Adds minutes to a time in HHMM format, with wrap-around at 24 hours.
     * @param hhmm The original time.
     * @param minutesToAdd Minutes to add.
     * @return The new time in HHMM format.
     */
    public static int addMinutesHHMM(int hhmm, int minutesToAdd) {
        int hours = hhmm / 100;
        int minutes = hhmm % 100;
        minutes += minutesToAdd;
        hours += minutes / 60;
        minutes = minutes % 60;
        hours = hours % 24;
        return hours * 100 + minutes;
    }
}
