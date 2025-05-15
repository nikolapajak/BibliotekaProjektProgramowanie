public class Book extends Item implements Loanable {
    private String author;

    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public void borrow(User user) throws InvalidItemException, OverdueException {
        if (isBorrowed()) {
            throw new InvalidItemException("Książka jest już wypożyczona.");
        }
        setBorrowed(true);
    }

    @Override
    public void returnItem() throws InvalidItemException, OverdueException {
        if (!isBorrowed()) {
            throw new InvalidItemException("Książka nie była wypożyczona.");
        }
        setBorrowed(false);
    }

    @Override
    public String displayDetails() {
        return getId() + " - " + getTitle() + " (Autor: " + author + ")" + (isBorrowed() ? " (wypożyczona)" : "");
    }

    @Override
    public String toString() {
        return displayDetails();
    }
}
