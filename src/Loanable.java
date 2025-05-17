public interface Loanable {
    void borrow(User user) throws Exception;
    void returnItem() throws Exception;
}
