import java.util.PriorityQueue;
import java.util.Comparator;

public class HybridStation {
    private String name;
    private PriorityQueue<HybridTrain> trainQueue;

    public HybridStation(String name) {
        this.name = name;
        this.trainQueue = new PriorityQueue<>(Comparator.comparingInt(HybridTrain::getDepartureTime));
    }

    public String getName() {
        return name;
    }

    public PriorityQueue<HybridTrain> getTrainQueue() {
        return trainQueue;
    }

    public void addTrain(HybridTrain train) {
        trainQueue.add(train);
    }

    public HybridTrain getNextTrain() {
        return trainQueue.peek();
    }

    public void removeTrain(HybridTrain train) {
        trainQueue.remove(train);
    }
}
