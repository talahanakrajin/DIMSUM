public final class TrainUtils {
    private TrainUtils() {}

    /**
     * Validates and formats a train ID to uppercase TSxxxx.
     * @param trainID The input train ID.
     * @return The formatted train ID.
     * @throws IllegalArgumentException if the format is invalid.
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
     * @param sc Scanner for user input.
     * @return Validated and formatted train ID.
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
}
