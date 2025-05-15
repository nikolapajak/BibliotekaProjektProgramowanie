import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca użytkownika biblioteki.
 */
public class User extends Human {
    private List<Item> borrowedItems;

    public User(String firstName, String lastName) {
        super(firstName, lastName);
        this.borrowedItems = new ArrayList<>();
    }

    public void borrowItem(Item item) {
        borrowedItems.add(item);
    }

    public void returnItem(Item item) {
        borrowedItems.remove(item);
    }

    /**
     * Sprawdza, czy użytkownik ma przeterminowane wypożyczenia.
     * Aktualna implementacja jest tymczasowa i zawsze zwraca false.
     * Rozbuduj ją, odwołując się do rzeczywistych rekordów wypożyczeń.
     */
    public boolean hasOverdueItems() {
        // TODO: rozbuduj tę metodę zgodnie z logiką przeterminowanych wypożyczeń
        return false;
    }

    public List<Item> getBorrowedItems() {
        return borrowedItems;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
