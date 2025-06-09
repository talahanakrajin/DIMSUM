import java.util.Scanner;

public class HybridScheduleMain {
    public static void main(String[] args) {
        HybridScheduleManager manager = new HybridScheduleManager();
        Scanner sc = new Scanner(System.in);

        // Add some trains
        manager.addTrain(new HybridTrain("TS0001", 800, "Lebak Bulus", 1));
        manager.addTrain(new HybridTrain("TS0002", 815, "Blok M", 2));
        manager.addTrain(new HybridTrain("TS0003", 830, "Bundaran HI", 3));
        manager.addTrain(new HybridTrain("TS0004", 845, "Blok M", 4));

        System.out.println("=== All Schedules ===");
        manager.printAllSchedules();

        System.out.println("\n=== Schedules for Blok M ===");
        manager.printStationSchedule("Blok M");

        System.out.println("\n=== Next Train (Global) ===");
        System.out.println(manager.getNextTrain());
        System.out.println("\n=== Next Train at Blok M ===");
        System.out.println(manager.getNextTrain("Blok M"));

        // Reschedule a train
        System.out.println("\nRescheduling TS0002 to 900 at Bundaran HI...");
        manager.rescheduleTrain("TS0002", 900, "Bundaran HI");
        manager.printAllSchedules();

        // Delay a train
        System.out.println("\nDelaying TS0001 by 20 minutes...");
        manager.delayTrain("TS0001", 20);
        manager.printAllSchedules();

        // Cancel a train
        System.out.println("\nCancelling TS0003...");
        manager.cancelTrain("TS0003");
        manager.printAllSchedules();

        // Test multiple delays and print delayed trains by priority
        System.out.println("\nDelaying TS0004 by 50 minutes...");
        manager.delayTrain("TS0004", 50);
        System.out.println("Delaying TS0002 by 10 minutes...");
        manager.delayTrain("TS0002", 10);

        // Print all delayed trains by priority
        System.out.println("\n=== All Delayed Trains by Priority ===");
        manager.printDelayedTrainsByPriority();

        // Additional: Print all schedules and per-station after all operations
        System.out.println("\n=== All Schedules After All Operations ===");
        manager.printAllSchedules();
        System.out.println("\n=== Schedules for Bundaran HI ===");
        manager.printStationSchedule("Bundaran HI");
        System.out.println("\n=== Schedules for Lebak Bulus ===");
        manager.printStationSchedule("Lebak Bulus");

        System.out.println("\n=== Next Train (Global) ===");
        System.out.println(manager.getNextTrain());

        System.out.println("\n=== Next Train at Blok M ===");
        System.out.println(manager.getNextTrain("Blok M"));
        sc.close();
    }
}
