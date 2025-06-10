import java.util.Scanner;

public class Passenger extends User {
    public Passenger(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Passenger: " + getName();
    }

    @Override
    public void accessSystem(SchedulingSystem scheduleSystem, Scanner sc) {
        scheduleSystem.passengerMenu(sc);
    }
}
