public class HybridTrain {
    private String trainID;
    private int departureTime;
    private String station;
    private int priority;

    public HybridTrain(String trainID, int departureTime, String station, int priority) {
        this.trainID = trainID;
        this.departureTime = departureTime;
        this.station = station;
        this.priority = priority;
    }

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TrainID: " + trainID + ", Departure: " + String.format("%04d", departureTime) +
                ", Station: " + station + ", Priority: " + priority;
    }
}
