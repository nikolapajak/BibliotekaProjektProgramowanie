import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Library class contains the collection of items and provides methods to manage them (add, remove, borrow, return).
 * It uses a Map for items (keyed by UUID) and a Set for tracking borrowed items.
 */
public class Library {
    private Map<UUID, Item> items;
    private Set<Item> borrowedItems;
    private Map<String, User> users = new HashMap<>();

    public Library() {
        this.items = new HashMap<>();
        this.borrowedItems = new HashSet<>();
    }
    public Map<UUID, Item> getItems() {
        return items;
    }
    public Set<Item> getBorrowedItems() {
        return borrowedItems;
    }
    /**
     * Adds a new Book to the library.
     */
    public void addBook(String title, int year, String author, String genre) throws InvalidItemException {
        Book book = new Book(title, year, author, genre);
        addItemToLibrary(book);
    }
    /**
     * Adds a new Magazine to the library.
     */
    public void addMagazine(String title, int year, int issueNumber) throws InvalidItemException {
        Magazine mag = new Magazine(title, year, issueNumber);
        addItemToLibrary(mag);
    }
    /**
     * Internal helper to add an Item to collections.
     */
    private void addItemToLibrary(Item item) throws InvalidItemException {
        if (items.containsKey(item.getId())) {
            throw new InvalidItemException("Przedmiot o ID " + item.getId() + " już istnieje.");
        }
        items.put(item.getId(), item);
        if (item.isBorrowed()) {
            borrowedItems.add(item);
        }
        // Save changes to files
        try {
            LibraryFileManager.saveLibrary(this);
        } catch (Exception e) {
            System.err.println("Błąd zapisu danych: " + e.getMessage());
        }
    }
    /**
     * Removes an item from the library by UUID.
     * @throws InvalidItemException if item not found or currently borrowed.
     */
    public void removeItem(UUID itemId) throws InvalidItemException {
        Item item = items.get(itemId);
        if (item == null) {
            throw new InvalidItemException("Przedmiot o ID " + itemId + " nie istnieje.");
        }
        if (item.isBorrowed()) {
            throw new InvalidItemException("Nie można usunąć - przedmiot jest wypożyczony.");
        }
        items.remove(itemId);
        borrowedItems.remove(item);
        // Note: if item was on loan, ideally handle loan records (not allowed removal if on loan).
        try {
            LibraryFileManager.saveLibrary(this);
        } catch (Exception e) {
            System.err.println("Błąd zapisu danych: " + e.getMessage());
        }
    }
    /**
     * Borrows an item for a user.
     * @throws OverdueException if the user has overdue items.
     * @throws InvalidItemException if item not found, not loanable, or already borrowed.
     */
    public void borrowItem(User user, UUID itemId) throws OverdueException, InvalidItemException {
        // Check user eligibility
        if (user.hasOverdueItems()) {
            throw new OverdueException("Masz przetrzymane (nieterminowo zwrócone) pozycje - nie możesz wypożyczyć kolejnej.");
        }
        Item item = items.get(itemId);
        if (item == null) {
            throw new InvalidItemException("Wybrany przedmiot nie istnieje w bibliotece.");
        }
        if (!(item instanceof Loanable)) {
            throw new InvalidItemException("Tego przedmiotu nie można wypożyczyć.");
        }
        Loanable loanableItem = (Loanable) item;
        // Attempt to mark as borrowed
        loanableItem.borrow(); // may throw InvalidItemException if already borrowed
        // Record the loan
        LoanRecord record = new LoanRecord(item);
        user.getLoanHistory().add(record);
        borrowedItems.add(item);
        // Save updated state
        try {
            LibraryFileManager.saveLibrary(this);
        } catch (Exception e) {
            System.err.println("Błąd zapisu danych: " + e.getMessage());
        }
    }
    /**
     * Returns an item that was borrowed by a user.
     * @throws OverdueException if the item is returned past its due date.
     * @throws InvalidItemException if item not found or not borrowed by this user.
     */
    public void returnItem(User user, UUID itemId) throws OverdueException, InvalidItemException {
        Item item = items.get(itemId);
        if (item == null) {
            throw new InvalidItemException("Przedmiot o ID " + itemId + " nie istnieje.");
        }
        if (!(item instanceof Loanable)) {
            throw new InvalidItemException("Tego przedmiotu nie można wypożyczyć ani zwrócić.");
        }
        // Find the loan record for this user and item
        LoanRecord foundRecord = null;
        for (LoanRecord rec : user.getLoanHistory()) {
            if (rec.getItem().equals(item) && !rec.isReturned()) {
                foundRecord = rec;
                break;
            }
        }
        if (foundRecord == null) {
            throw new InvalidItemException("Ten użytkownik nie wypożyczył wskazanego przedmiotu.");
        }
        // Mark the item as returned
        Loanable loanableItem = (Loanable) item;
        loanableItem.returnItem();
        foundRecord.setReturnDate(java.time.LocalDate.now());
        borrowedItems.remove(item);
        // Check for overdue return
        if (foundRecord.getReturnDate().isAfter(foundRecord.getDueDate())) {
            // Save state and then throw overdue
            try {
                LibraryFileManager.saveLibrary(this);
            } catch (Exception e) {
                System.err.println("Błąd zapisu danych: " + e.getMessage());
            }
            throw new OverdueException("Zwrócono po terminie! Przedmiot \"" + item.getTitle() + "\" był przetrzymany.");
        }
        // Save state after normal return
        try {
            LibraryFileManager.saveLibrary(this);
        } catch (Exception e) {
            System.err.println("Błąd zapisu danych: " + e.getMessage());
        }
    }
    public Map<String, User> getUsers() {
        return users;
    }
    
    public void addUser(User user) {
        users.put(user.getName(), user);
    }
    
}
