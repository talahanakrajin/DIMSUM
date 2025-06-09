package PriorityQueue;
public class PerformanceMetrics {
    public static void measureRuntime(Runnable function) {
        System.gc();
        double startTime = System.nanoTime();

        Runtime runtime = Runtime.getRuntime();
        double memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        function.run();

        double memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        double endTime = System.nanoTime();

        double timeTaken = (endTime - startTime) / 1_000_000.0;
        double memoryUsed = (memoryAfter - memoryBefore) / 1024.0;

        System.out.println("Time taken: " + timeTaken + " ms");
        System.out.println("Memory used: " + memoryUsed + " KB\n");
    }

}
