import java.util.ArrayList;
import java.util.List;

/**
 * User is a library patron who can borrow items and has a record of loans.
 */
public class User extends Human implements java.io.Serializable {

    private List<LoanRecord> loanHistory;

    public User(String name) {
        super(name);
        this.loanHistory = new ArrayList<>();
    }
    public List<LoanRecord> getLoanHistory() {
        return loanHistory;
    }
    /**
     * Checks if this user currently has any overdue items.
     * @return true if an item is borrowed past its due date, false otherwise.
     */
    public boolean hasOverdueItems() {
        java.time.LocalDate today = java.time.LocalDate.now();
        for (LoanRecord record : loanHistory) {
            if (!record.isReturned() && record.getDueDate().isBefore(today)) {
                return true;
            }
        }
        return false;
    }
}
