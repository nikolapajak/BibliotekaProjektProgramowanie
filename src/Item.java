public abstract class Item {
    private String id;
    private String title;
    protected boolean borrowed;

    public Item(String id, String title) {
        this.id = id;
        this.title = title;
        this.borrowed = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public abstract String displayDetails();

    @Override
    public String toString() {
        return id + " - " + title + (borrowed ? " (wypożyczone)" : "");
    }
}
