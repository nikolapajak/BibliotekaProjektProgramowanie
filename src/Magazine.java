/**
 * Magazine is a type of Item that is loanable and has an issue number.
 */
public class Magazine extends Item implements Loanable {
    private static final long serialVersionUID = 1L;
    private int issueNumber;

    public Magazine(String title, int year, int issueNumber) {
        super(title, year);
        this.issueNumber = issueNumber;
    }
    public Magazine(java.util.UUID id, String title, int year, int issueNumber) {
        super(id, title, year);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public String displayDetails() {
        String details = "Czasopismo: \"" + title + "\" (" + year + "), numer: " + issueNumber;
        if (borrowed) {
            details += " [WYPOŻYCZONE]";
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
