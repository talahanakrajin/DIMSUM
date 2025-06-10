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
    public void accessSystem(SchedulingSystem manager, java.util.Scanner sc) {
        manager.managerMenu(sc);
    }
}
