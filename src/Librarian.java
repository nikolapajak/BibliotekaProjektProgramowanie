/**
 * Klasa reprezentująca bibliotekarza.
 */
public class Librarian extends Human {
    private String employeeId;

    public Librarian(String firstName, String lastName, String employeeId) {
        super(firstName, lastName);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDetails() {
        return "Bibliotekarz: " + getFullName() + " (ID: " + employeeId + ")";
    }
}
