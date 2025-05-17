import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibraryGUI extends JFrame {
    private String role;
    private Library library;
    private JTable table;
    private ItemTableModel tableModel;
    private User currentUser;
    private Librarian currentLibrarian;

    public LibraryGUI(String role, String firstName, String lastName) {
        super("System biblioteczny");
        this.role = role.toLowerCase();
        library = new Library();

        // Załaduj dane z pliku
        List<Item> loadedItems = LibraryFileManager.loadItemsFromFile();
        for (Item item : loadedItems) {
            library.addItem(item);
        }

        if (this.role.equals("user")) {
            currentUser = new User(firstName, lastName);
            library.addUser(currentUser);
        } else if (this.role.equals("librarian")) {
            currentLibrarian = new Librarian(firstName, lastName, 0);
            library.addLibrarian(currentLibrarian);
        }

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> {
            LibraryFileManager.saveItemsToFile(library.getAllItems());
            dispose();
            new LoginScreen();
        });

        JTextField searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText().toLowerCase();
                tableModel.filter(text);
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Szukaj:"));
        searchPanel.add(searchField);

        topPanel.add(logoutButton, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new ItemTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        for (Item item : library.getAllItems()) {
            tableModel.addItem(item);
        }

        if (role.equalsIgnoreCase("user")) {
            JButton borrowButton = new JButton("Wypożycz");
            JButton returnButton = new JButton("Zwróć");

            borrowButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Item item = tableModel.getItemAt(row);
                    try {
                        library.borrowItem(UUID.fromString(item.getId()), currentUser);
                        tableModel.fireTableDataChanged();
                        LibraryFileManager.saveItemsToFile(library.getAllItems());
                        JOptionPane.showMessageDialog(this, "Wypożyczono: " + item.getTitle());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Proszę wybrać przedmiot do wypożyczenia.");
                }
            });

            returnButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Item item = tableModel.getItemAt(row);
                    try {
                        library.returnItem(UUID.fromString(item.getId()), currentUser);
                        tableModel.fireTableDataChanged();
                        LibraryFileManager.saveItemsToFile(library.getAllItems());
                        JOptionPane.showMessageDialog(this, "Zwrócono: " + item.getTitle());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Proszę wybrać przedmiot do zwrotu.");
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(borrowButton);
            buttonPanel.add(returnButton);

            add(scrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

        } else if (role.equalsIgnoreCase("librarian")) {
            JTextField titleField = new JTextField(15);
            JTextField authorField = new JTextField(15);
            JTextField yearField = new JTextField(5);
            JTextField genreField = new JTextField(15);

            JButton addBookButton = new JButton("Dodaj książkę");
            JButton removeButton = new JButton("Usuń zaznaczoną");

            addBookButton.addActionListener(e -> {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String yearText = yearField.getText().trim();
                String genre = genreField.getText().trim();

                if (title.isEmpty() || author.isEmpty() || yearText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola (gatunek może być pusty).");
                    return;
                }

                try {
                    int year = Integer.parseInt(yearText);
                    UUID id = UUID.randomUUID();

                    Book book = new Book(id, title, year, author, genre);
                    library.addItem(book);
                    tableModel.addItem(book);

                    // Wyczyść pola
                    titleField.setText("");
                    authorField.setText("");
                    yearField.setText("");
                    genreField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Rok musi być liczbą całkowitą.");
                }
            });

            removeButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Item item = tableModel.getItemAt(row);
                    library.removeItem(item);
                    tableModel.removeItemAt(row);
                } else {
                    JOptionPane.showMessageDialog(this, "Proszę wybrać przedmiot do usunięcia.");
                }
            });

            JPanel formPanel = new JPanel();
            formPanel.add(new JLabel("Tytuł:"));
            formPanel.add(titleField);
            formPanel.add(new JLabel("Autor:"));
            formPanel.add(authorField);
            formPanel.add(new JLabel("Rok:"));
            formPanel.add(yearField);
            formPanel.add(new JLabel("Gatunek:"));
            formPanel.add(genreField);
            formPanel.add(addBookButton);
            formPanel.add(removeButton);

            add(scrollPane, BorderLayout.CENTER);
            add(formPanel, BorderLayout.SOUTH);
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static class ItemTableModel extends AbstractTableModel {
        private final String[] columnNames = {"UUID", "Tytuł", "Autor", "Rok", "Gatunek", "Status"};
        private final List<Item> items = new ArrayList<>();
        private final List<Item> allItems = new ArrayList<>();

        public Item getItemAt(int row) {
            return items.get(row);
        }

        public void addItem(Item item) {
            items.add(item);
            allItems.add(item);
            fireTableRowsInserted(items.size() - 1, items.size() - 1);
        }

        public void removeItemAt(int row) {
            Item toRemove = items.get(row);
            allItems.remove(toRemove);
            items.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public void filter(String text) {
            items.clear();
            if (text.isEmpty()) {
                items.addAll(allItems);
            } else {
                String lowerText = text.toLowerCase();
                for (Item item : allItems) {
                    String content = item.getId().toString().toLowerCase() + " " +
                            item.getTitle().toLowerCase() + " " +
                            (item instanceof Book ? ((Book) item).getAuthor().toLowerCase() + " " + ((Book) item).getGenre().toLowerCase() : "") + " " +
                            String.valueOf(item.getYear());
                    if (content.contains(lowerText)) {
                        items.add(item);
                    }
                }
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Item item = items.get(rowIndex);
            switch (columnIndex) {
                case 0: return item.getId().toString();
                case 1: return item.getTitle();
                case 2: return (item instanceof Book) ? ((Book) item).getAuthor() : "-";
                case 3: return item.getYear();
                case 4: return (item instanceof Book) ? ((Book) item).getGenre() : "-";
                case 5: return item.isBorrowed() ? "wypożyczona" : "dostępna";
                default: return "";
            }
        }
    }
}
