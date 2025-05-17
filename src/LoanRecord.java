import java.io.Serializable;
import java.time.LocalDate;

/**
 * LoanRecord represents a single borrowing transaction of an item by a user.
 * It contains the item, borrow date, due date, and return date (if returned).
 */
public class LoanRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int LOAN_PERIOD_DAYS = 14;

    private Item item;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public LoanRecord(Item item) {
        this.item = item;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(LOAN_PERIOD_DAYS);
        this.returnDate = null;
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
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public boolean isReturned() {
        return returnDate != null;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns a string describing this loan record, including dates.
     */
    @Override
    public String toString() {
        String itemTitle = item.getTitle();
        String itemType = (item instanceof Book ? "Książka" : (item instanceof Magazine ? "Czasopismo" : "Pozycja"));
        String info = itemType + " \"" + itemTitle + "\" - wypożyczono: " + borrowDate;
        if (returnDate != null) {
            info += ", zwrócono: " + returnDate;
        } else {
            info += ", nie zwrócono";
            // indicate overdue if past due date
            if (LocalDate.now().isAfter(dueDate)) {
                info += " (PRZETERMINOWANE)";
            }
        }
        return info;
    }
}
