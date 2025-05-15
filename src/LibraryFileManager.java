import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryFileManager {
    private static final String FILE_NAME = "library_items.txt";

    // Zapisz listę przedmiotów (Book, Magazine) do pliku
    public static void saveItemsToFile(List<Item> items) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Item item : items) {
                String type = "";
                String author = "";
                if (item instanceof Book) {
                    type = "BOOK";
                    author = ((Book) item).getAuthor();
                } else if (item instanceof Magazine) {
                    type = "MAGAZINE";
                } else {
                    continue; // inne typy ignoruj
                }
                writer.println(type + ";" + item.getId() + ";" + item.getTitle() + ";" + author + ";" + item.isBorrowed());
            }
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    // Wczytaj listę przedmiotów z pliku
    public static List<Item> loadItemsFromFile() {
        List<Item> items = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String type = parts[0];
                    String id = parts[1];
                    String title = parts[2];
                    String author = parts.length > 4 ? parts[3] : "";
                    boolean borrowed = Boolean.parseBoolean(parts[parts.length - 1]);

                    Item item = null;
                    if (type.equals("BOOK")) {
                        item = new Book(id, title, author);
                    } else if (type.equals("MAGAZINE")) {
                        item = new Magazine(id, title);
                    }

                    if (item != null) {
                        item.setBorrowed(borrowed);
                        items.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu z pliku: " + e.getMessage());
        }
        return items;
    }
}
