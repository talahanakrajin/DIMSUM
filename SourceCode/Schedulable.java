package SourceCode;
/**
 * Interface for schedulable entities in the MRT system.
 * This interface defines the basic contract for any entity that can be scheduled,
 * requiring implementation of methods for managing departure times.
 * Demonstrates use of interfaces and polymorphism.
 */
public interface Schedulable {
    /**
     * Gets the departure time of the schedulable entity.
     * @return The departure time in HHMM format
     */
    int getDepartureTime();

    /**
     * Sets the departure time of the schedulable entity.
     * @param time The new departure time in HHMM format
     */
    void setDepartureTime(int time);
}
