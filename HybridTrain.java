public class HybridTrain {
    private String trainID;
    private int departureTime;
    private String CurrentStation;
    private int delay = 0;

    public HybridTrain(String trainID, int departureTime, String station, int unusedPriority) {
        this.trainID = trainID;
        this.departureTime = departureTime;
        this.CurrentStation = station;
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
        return CurrentStation;
    }

    public void setStation(String station) {
        this.CurrentStation = station;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "TrainID: " + trainID + ", Departure: " + String.format("%04d", departureTime) +
                ", Station: " + CurrentStation;
    }
}
