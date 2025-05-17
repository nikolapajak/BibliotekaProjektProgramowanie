import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract class representing a general library item with a unique ID.
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    protected UUID id;
    protected String title;
    protected int year;
    protected boolean borrowed;

    public Item(String title, int year) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.year = year;
        this.borrowed = false;
    }

    public Item(UUID id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.borrowed = false;
    }

    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public boolean isBorrowed() {
        return borrowed;
    }

    public abstract String displayDetails();

    @Override
    public String toString() {
        return displayDetails();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Item)) return false;
        Item other = (Item) o;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
