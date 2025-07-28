import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationManager {

    public static void saveTasks(List<Task> tasks, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Task> loadTasks(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); 
            return new ArrayList<>(); 
        }
    }
}
