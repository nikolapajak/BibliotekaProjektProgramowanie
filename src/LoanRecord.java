import java.time.LocalDate;

public class LoanRecord {
    private final User user;
    private final Item item;
    private final LocalDate loanDate;
    private LocalDate returnDate;

    public LoanRecord(User user, Item item) {
        this.user = user;
        this.item = item;
        this.loanDate = LocalDate.now();
        this.returnDate = null;
    }

    public User getUser() { return user; }
    public Item getItem() { return item; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {
        if (returnDate != null) return false;
        return loanDate.plusWeeks(2).isBefore(LocalDate.now()); // 2 tygodnie termin zwrotu
    }
}
