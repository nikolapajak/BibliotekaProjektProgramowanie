import java.util.ArrayList;
import java.util.List;

public class User extends Human {
    private List<Item> borrowedItems = new ArrayList<>();

    public User(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public List<Item> getBorrowedItems() {
        return borrowedItems;
    }

    public void borrowItem(Item item) throws InvalidItemException {
        if (item.isBorrowed()) throw new InvalidItemException("Przedmiot jest już wypożyczony.");
        item.setBorrowed(true);
        borrowedItems.add(item);
    }

    public void returnItem(Item item) throws InvalidItemException {
        if (!borrowedItems.contains(item)) throw new InvalidItemException("Przedmiot nie jest wypożyczony przez użytkownika.");
        item.setBorrowed(false);
        borrowedItems.remove(item);
    }
}
