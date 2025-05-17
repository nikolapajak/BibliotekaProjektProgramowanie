/**
 * Book is a type of Item that is loanable and has an author and genre.
 */
public class Book extends Item implements Loanable {
    private static final long serialVersionUID = 1L;
    private String author;
    private String genre;

    public Book(String title, int year, String author, String genre) {
        super(title, year);
        this.author = author;
        this.genre = genre;
    }
    public Book(java.util.UUID id, String title, int year, String author, String genre) {
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
    public String displayDetails() {
        String details = "Książka: \"" + title + "\" (" + year + "), autor: " + author + ", gatunek: " + genre;
        if (borrowed) {
            details += " [WYPOŻYCZONA]";
        }
        return details;
    }

    @Override
    public void borrow() throws InvalidItemException {
        if (borrowed) {
            throw new InvalidItemException("Przedmiot \"" + title + "\" jest już wypożyczony.");
        }
        borrowed = true;
    }

    @Override
    public void returnItem() throws InvalidItemException {
        if (!borrowed) {
            throw new InvalidItemException("Przedmiot \"" + title + "\" nie jest aktualnie wypożyczony.");
        }
        borrowed = false;
    }
    
}
