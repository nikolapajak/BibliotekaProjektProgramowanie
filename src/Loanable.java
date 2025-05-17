// Interface defining borrowing/returnable behavior for library items
public interface Loanable {
    /**
     * Borrows the item (marks as borrowed).
     * @throws InvalidItemException if the item is already borrowed or cannot be borrowed
     */
    void borrow() throws InvalidItemException;
    /**
     * Returns the item (marks as returned).
     * @throws InvalidItemException if the item is not currently borrowed
     */
    void returnItem() throws InvalidItemException;
}
