import java.time.LocalDate;

/**
 * Klasa reprezentująca pojedynczy zapis wypożyczenia.
 */
public class LoanRecord {
    private User user;
    private Item item;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public LoanRecord(User user, Item item, LocalDate borrowDate, LocalDate dueDate) {
        this.user = user;
        this.item = item;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }
}
