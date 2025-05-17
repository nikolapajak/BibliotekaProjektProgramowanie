import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for saving and loading library data to a text or binary file.
 */
public class LibraryFileManager {
    private static final String TEXT_FILE = "library_items.txt";
    private static final String BINARY_FILE = "library_items.dat";
    

    /**
     * Saves the library's items to both a text file and a binary file.
     */
    public static void saveLibrary(Library library) throws IOException {
        // Save to text file
        try (PrintWriter pw = new PrintWriter(new FileWriter(TEXT_FILE))) {
            for (Item item : library.getItems().values()) {
                if (item instanceof Book) {
                    Book b = (Book) item;
                    // Format: BOOK;UUID;Title;Year;Author;Genre;BorrowedFlag
                    pw.println("BOOK;" + b.getId() + ";" + b.getTitle() + ";" + b.getYear() + ";" 
                               + b.getAuthor() + ";" + b.getGenre() + ";" + b.isBorrowed());
                } else if (item instanceof Magazine) {
                    Magazine m = (Magazine) item;
                    // Format: MAGAZINE;UUID;Title;Year;IssueNumber;BorrowedFlag
                    pw.println("MAGAZINE;" + m.getId() + ";" + m.getTitle() + ";" + m.getYear() + ";" 
                               + m.getIssueNumber() + ";" + m.isBorrowed());
                }
            }
        }
        // Save to binary file (serialize the items map)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_FILE))) {
            oos.writeObject(library.getItems());
        }
    }

    /**
     * Loads library items from file (prefers binary if available, otherwise text).
     */
    @SuppressWarnings("unchecked")
    public static void loadLibrary(Library library) throws IOException {
        File bin = new File(BINARY_FILE);
        File txt = new File(TEXT_FILE);
        boolean loaded = false;
        if (bin.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bin))) {
                Object obj = ois.readObject();
                if (obj instanceof Map) {
                    Map<UUID, Item> loadedMap = (Map<UUID, Item>) obj;
                    for (Item item : loadedMap.values()) {
                        // Add each item to library (preserving borrowed status)
                        // We assume items in file already have correct borrowed field.
                        library.getItems().put(item.getId(), item);
                        if (item.isBorrowed()) {
                            library.getBorrowedItems().add(item);
                        }
                    }
                    loaded = true;
                }
            } catch (ClassNotFoundException e) {
                // If classes not found or mismatch, fallback to text
                loaded = false;
            }
        }
        if (!loaded && txt.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length > 0) {
                        String type = parts[0];
                        try {
                            if ("BOOK".equals(type) && parts.length == 7) {
                                UUID id = UUID.fromString(parts[1]);
                                String title = parts[2];
                                int year = Integer.parseInt(parts[3]);
                                String author = parts[4];
                                String genre = parts[5];
                                boolean isBorrowed = Boolean.parseBoolean(parts[6]);
                                Book book = new Book(id, title, year, author, genre);
                                if (isBorrowed) {
                                    book.borrow();
                                }
                                library.getItems().put(id, book);
                                if (book.isBorrowed()) {
                                    library.getBorrowedItems().add(book);
                                }
                            } else if ("MAGAZINE".equals(type) && parts.length == 6) {
                                UUID id = UUID.fromString(parts[1]);
                                String title = parts[2];
                                int year = Integer.parseInt(parts[3]);
                                int issue = Integer.parseInt(parts[4]);
                                boolean isBorrowed = Boolean.parseBoolean(parts[5]);
                                Magazine mag = new Magazine(id, title, year, issue);
                                if (isBorrowed) {
                                    mag.borrow();
                                }
                                library.getItems().put(id, mag);
                                if (mag.isBorrowed()) {
                                    library.getBorrowedItems().add(mag);
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println("Błąd odczytu danych: " + ex.getMessage());
                        }
                    }
                }
            }
        }
    }
}
