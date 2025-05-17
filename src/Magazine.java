import java.util.UUID;

public class Magazine extends Item implements Loanable {
    private int issueNumber;

    public Magazine(UUID id, String title, int year, int issueNumber) {
        super(id, title, year);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public void borrow(User user) throws InvalidItemException {
        if (isBorrowed()) {
            throw new InvalidItemException("Magazyn jest już wypożyczony.");
        }
        setBorrowed(true);
    }

    @Override
    public void returnItem() throws InvalidItemException {
        if (!isBorrowed()) {
            throw new InvalidItemException("Magazyn nie był wypożyczony.");
        }
        setBorrowed(false);
    }

    @Override
    public String displayDetails() {
        return getId().toString() + " - " + getTitle() + " (Rok: " + getYear() + ", Nr wydania: " + issueNumber + ")" + (isBorrowed() ? " (wypożyczony)" : "");
    }
}
