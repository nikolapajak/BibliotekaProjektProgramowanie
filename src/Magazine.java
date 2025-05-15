public class Magazine extends Item implements Loanable {

    public Magazine(String id, String title) {
        super(id, title);
    }

    @Override
    public void borrow(User user) throws InvalidItemException, OverdueException {
        if (isBorrowed()) {
            throw new InvalidItemException("Magazyn jest już wypożyczony.");
        }
        setBorrowed(true);
    }

    @Override
    public void returnItem() throws InvalidItemException, OverdueException {
        if (!isBorrowed()) {
            throw new InvalidItemException("Magazyn nie był wypożyczony.");
        }
        setBorrowed(false);
    }

    @Override
    public String displayDetails() {
        return getId() + " - " + getTitle() + (isBorrowed() ? " (wypożyczony)" : "");
    }

    @Override
    public String toString() {
        return displayDetails();
    }
}
