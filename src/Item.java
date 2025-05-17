import java.util.UUID;

public abstract class Item {
    private UUID id;
    private String title;
    private int year;
    protected boolean borrowed;

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

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public abstract String displayDetails();

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return displayDetails();
    }
}
