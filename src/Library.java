import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Library {
    private List<Item> items = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Librarian> librarians = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
        LibraryFileManager.saveItemsToFile(items); // Zapis po dodaniu
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addLibrarian(Librarian librarian) {
        librarians.add(librarian);
    }

    public List<Item> getAllItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void borrowItem(UUID id, User user) throws InvalidItemException {
        Item item = findItemById(id);
        if (item == null) throw new InvalidItemException("Nie znaleziono przedmiotu.");
        if (item.isBorrowed()) throw new InvalidItemException("Przedmiot jest już wypożyczony.");
        item.setBorrowed(true);
        user.getBorrowedItems().add(item);
        LibraryFileManager.saveItemsToFile(items); // Zapis po wypożyczeniu
    }

    public void returnItem(UUID id, User user) throws InvalidItemException {
        Item item = findItemById(id);
        if (item == null) throw new InvalidItemException("Nie znaleziono przedmiotu.");
        if (!item.isBorrowed()) throw new InvalidItemException("Przedmiot nie jest wypożyczony.");
        item.setBorrowed(false);
        user.getBorrowedItems().remove(item);
        LibraryFileManager.saveItemsToFile(items); // Zapis po zwrocie
    }

    public void removeItem(Item item) {
        items.remove(item);
        LibraryFileManager.saveItemsToFile(items); // Zapis po usunięciu
    }

    private Item findItemById(UUID id) {
        for (Item item : items) {
            if (UUID.fromString(item.getId()).equals(id)) return item;
        }
        return null;
    }
}
