/**
 * MRT is a concrete train type, extending Trains.
 * Demonstrates inheritance and polymorphism.
 */
public class MRT extends Trains {
    /**
     * Constructor for MRT.
     * @param trainID Unique identifier for the train.
     * @param departureTime Departure time in HHMM format.
     * @param currentStation Name of the current station.
     */
    public MRT(String trainID, int departureTime, String currentStation) {
        super(trainID, departureTime, currentStation);
    }

    /** MRT-specific operation (polymorphism) */
    @Override
    public void operate() {
        // MRT-specific operation logic (placeholder)
    }
}
