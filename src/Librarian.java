/**
 * Librarian is a type of Human with a salary, representing a library staff member.
 */
public class Librarian extends Human {
    private double salary;

    public Librarian(String name, double salary) {
        super(name);
        this.salary = salary;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
}
