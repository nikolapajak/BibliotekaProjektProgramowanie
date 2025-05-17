import java.util.UUID;

public class Book extends Item implements Loanable {
    private String author;
    private String genre;

    public Book(UUID id, String title, int year, String author, String genre) {
        super(id, title, year);
        this.author = author;
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
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
        return getId().toString() + " - " + getTitle() + " (Autor: " + author + ", Gatunek: " + genre + ")" + (isBorrowed() ? " (wypożyczona)" : "");
    }
}
