import java.util.TreeMap;

public class Train extends Stations {
    private String trainID;
    private int departureTime;

    private boolean isAvailable;
    private boolean isOnTime; 
    
    TreeMap<Integer, String> trainMap = new TreeMap<>(); // Integer = departure time, String = train ID

    // Constructor
    public Train(TreeMap<Integer, String> trainMap, String trainID, int departureTime) {
        this.trainMap = trainMap; 

        this.trainID = trainID;
        this.departureTime = departureTime;
        this.isAvailable = true; 
        this.isOnTime = true; 
    }

    public TreeMap<Integer, String> getTrainMap() {
        return trainMap;
    }
    
    public void add_ID_and_departure_time() {
        this.trainMap.put(this.departureTime, this.trainID); // Add the train ID to the TreeMap with the departure time as the key
    }

    public String getTrainID() {
        return trainID;
    }
    public int getDepartureTime() {
        return departureTime;
    }
    public void setTrainID(String trainID) {
        if (trainID == null || !trainID.matches("TS[0-9]{4}")) { // Updated regex to explicitly match digits
            throw new IllegalArgumentException("Invalid train ID! Enter with the format TSxxxx. (x = non-negative integer)");
        }
        this.trainID = trainID;
    }
    public void setDepartureTime(int departureTime) {
        if (departureTime < 0 || departureTime > 2359) {
            throw new IllegalArgumentException("Invalid departure time! Enter between 0000-2359.");
        }
        else if (departureTime % 100 >= 60) {
            throw new IllegalArgumentException("Invalid departure time! Minutes must be between 00-59.");
        }
        this.departureTime = departureTime;
    }

    // IS AVAILABLE
    public void checkTrainAvailability() {
        if (isAvailable) {
            System.out.println(trainID + " is available.");
        } else {
            System.out.println(trainID + " is out of service.");
        }
    }
    public void setTrainAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // IS ON TIME
    public void checkTrainOnTime() {
        if (isOnTime) {
            System.out.println(trainID + " is on time.");
        } else {
            System.out.println(trainID + " is delayed.");
        }
    }
}
