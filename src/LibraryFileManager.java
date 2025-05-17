import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibraryFileManager {
    private static final String FILE_NAME = "library_items.dat";

    // Zapisz listę przedmiotów do pliku binarnego
    public static void saveItemsToFile(List<Item> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(items);
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    // Wczytaj listę przedmiotów z pliku binarnego
    @SuppressWarnings("unchecked")
    public static List<Item> loadItemsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Item>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd odczytu z pliku: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
