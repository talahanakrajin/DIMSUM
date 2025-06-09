public class Manager extends User {
    public Manager(String name, String id) {
        super(name, id);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "name='" + getName() + '\'' +
                ", id='" + getId() + '\'' +
                '}';
    }

    @Override
    // Only switch-case logic here
    public void accessSystem(SchedulingSystem manager, java.util.Scanner sc) {
        Stations staticStations = new Stations(); // For access to stationMap
        boolean running = true;
        while (running) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Add train");
            System.out.println("2. Reschedule train");
            System.out.println("3. Delay train");
            System.out.println("4. Cancel train");
            System.out.println("5. View all schedules");
            System.out.println("6. View schedules for a station");
            System.out.println("7. View delayed trains by priority");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    String id = TrainUtils.promptValidTrainID(sc);
                    int dep = TimeUtils.promptValidDepartureTime(sc);
                    System.out.print("Enter Station: ");
                    String station = sc.nextLine();
                    manager.addTrain(new MRT(id, dep, station));
                    System.out.println("Train added.");
                    break;
                case "2":
                    System.out.print("Enter Train ID: ");
                    String trainID = TrainUtils.promptValidTrainID(sc);
                    int newDep = TimeUtils.promptValidDepartureTime(sc);
                    int newStationNum = StationUtils.stationSelection(sc, staticStations.getStationMap());
                    String newStation;
                    if (newStationNum == 0) {
                        MRT train = (MRT) manager.getTrainById(trainID);
                        if (train != null) {
                            newStation = train.getCurrentStation();
                        } else {
                            newStation = null;
                        }
                    } else {
                        newStation = staticStations.getStationMap().get(newStationNum);
                    }
                    manager.rescheduleTrain(trainID, newDep, newStation);
                    System.out.println("Train rescheduled.");
                    break;
                case "3":
                    System.out.print("Enter Train ID: ");
                    String delayID = TrainUtils.promptValidTrainID(sc);
                    int delay = TimeUtils.promptValidDelayMinutes(sc);
                    manager.delayTrain(delayID, delay);
                    System.out.println("Train delayed.");
                    break;
                case "4":
                    System.out.print("Enter Train ID: ");
                    String cancelID = sc.nextLine();
                    manager.cancelTrain(cancelID);
                    System.out.println("Train cancelled.");
                    break;
                case "5":
                    manager.printAllSchedules();
                    break;
                case "6":
                    int stationNum = StationUtils.checkStationNumber(sc, staticStations.getStationMap());
                    String st = staticStations.getStationMap().get(stationNum);
                    manager.printStationSchedule(st);
                    break;
                case "7":
                    manager.printDelayedTrainsByPriority();
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
