public interface Loanable {
    void borrow(User user) throws InvalidItemException, OverdueException;
    void returnItem() throws InvalidItemException, OverdueException;
}
