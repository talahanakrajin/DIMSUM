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
        Stations staticStations = new Stations(); // For access to stationMap
        boolean running = true;
        while (running) {
            System.out.println("\nPassenger Menu:");
            System.out.println("1. View all schedules");
            System.out.println("2. View schedules for a station");
            System.out.println("3. View next train (global)");
            System.out.println("4. View next train at a station");
            System.out.println("5. View delayed trains by priority");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    scheduleSystem.printAllSchedules();
                    break;
                case "2":
                    int stationNum = Stations.checkStationNumber(sc, staticStations.getStationMap());
                    String station = staticStations.getStationMap().get(stationNum);
                    scheduleSystem.printStationSchedule(station);
                    break;
                case "3":
                    System.out.println(scheduleSystem.getNextTrain());
                    break;
                case "4":
                    int stNum = Stations.checkStationNumber(sc, staticStations.getStationMap());
                    String st = staticStations.getStationMap().get(stNum);
                    System.out.println(scheduleSystem.getNextTrain(st));
                    break;
                case "5":
                    scheduleSystem.printDelayedTrainsByPriority();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
