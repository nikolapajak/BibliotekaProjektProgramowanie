public class Librarian extends Human {
    private int salary;

    public Librarian(String firstName, String lastName, int salary) {
        super(firstName, lastName);
        this.salary = salary;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
