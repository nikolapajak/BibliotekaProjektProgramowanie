import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca bibliotekę.
 */
public class Library {
    private List<Item> items;
    private List<User> users;
    private List<LoanRecord> loans;

    public Library() {
        items = new ArrayList<>();
        users = new ArrayList<>();
        loans = new ArrayList<>();
        loadItems(); // Wczytaj elementy z pliku przy starcie
    }

    public void addItem(Item item) {
        items.add(item);
        saveItems(); // Zapisz do pliku po dodaniu
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void borrowItem(String itemId, User user) throws InvalidItemException, OverdueException {
        Item itemToBorrow = findItemById(itemId);
        if (itemToBorrow == null) {
            throw new InvalidItemException("Nie znaleziono przedmiotu o podanym ID.");
        }
        if (user.hasOverdueItems()) {
            throw new OverdueException("Użytkownik ma przeterminowane wypożyczenia.");
        }

        if (itemToBorrow instanceof Loanable) {
            ((Loanable) itemToBorrow).borrow(user);
            user.borrowItem(itemToBorrow);
            loans.add(new LoanRecord(user, itemToBorrow, java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(14)));
            saveItems(); // Zapisz po wypożyczeniu
        }
    }

    public void returnItem(String itemId, User user) throws InvalidItemException, OverdueException {
        Item itemToReturn = findItemById(itemId);
        if (itemToReturn == null) {
            throw new InvalidItemException("Nie znaleziono przedmiotu o podanym ID.");
        }

        if (itemToReturn instanceof Loanable) {
            ((Loanable) itemToReturn).returnItem();
            user.returnItem(itemToReturn);
            saveItems(); // Zapisz po zwrocie
        }
    }

    private Item findItemById(String id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        return items;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public List<LoanRecord> getAllLoans() {
        return loans;
    }

    // Wczytaj przedmioty z pliku
    private void loadItems() {
        List<Item> loadedItems = LibraryFileManager.loadItemsFromFile();
        items.clear();
        items.addAll(loadedItems);
    }

    // Zapisz przedmioty do pliku
    private void saveItems() {
        LibraryFileManager.saveItemsToFile(items);
    }
}
