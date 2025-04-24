import java.util.TreeMap;

public class Methods {
    public void addEntry(TreeMap<Integer, String> treeMap, int key, String value) {
        treeMap.put(key, value);
    }
    public void removeEntry(TreeMap<Integer, String> treeMap, int key) {
        treeMap.remove(key);
    }
}
