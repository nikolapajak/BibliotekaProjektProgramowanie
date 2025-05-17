/**
 * Human is a base class for people in the system (User or Librarian).
 */
public class Human {
    protected String name;

    public Human(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
