import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(500, "T001");
        treeMap.put(510, "T002");
        treeMap.put(530, "T004");
        treeMap.put(540, "T005");
        treeMap.put(600, "T006");

        // Display the TreeMap
        System.out.println("TreeMap: " + treeMap);

        Methods methods = new Methods(); 

        methods.addEntry(treeMap, 520, "T003");
        System.out.println("After adding T003: " + treeMap);
        methods.addEntry(treeMap, 530, "T007");
        System.out.println("After adding T007: " + treeMap);

        methods.removeEntry(treeMap, 510);
        System.out.println("After removing T002: " + treeMap);
        methods.removeEntry(treeMap, 600);
        System.out.println("After removing T006: " + treeMap);
    }
}
