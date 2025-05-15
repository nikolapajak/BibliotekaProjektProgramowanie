import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryGUI extends JFrame {
    private String role;

    private Library library;
    private JTable table;
    private ItemTableModel tableModel;
    private User currentUser;

    public LibraryGUI(String role) {
        super("System biblioteczny");
        this.role = role;
        initComponents();
    }

    private void initComponents() {
        library = new Library();
        setLayout(new BorderLayout());

        // Górny panel z przyciskiem wyloguj i polem wyszukiwania
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> {
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

        // Tabela i model tabeli
        tableModel = new ItemTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        if (role.equals("user")) {
            currentUser = new User("Jan", "Kowalski");
            library.addUser(currentUser);

            for (Item item : library.getAllItems()) {
                tableModel.addItem(item);
            }

            JButton borrowButton = new JButton("Wypożycz");
            JButton returnButton = new JButton("Zwróć");

            borrowButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Item item = tableModel.getItemAt(row);
                    try {
                        library.borrowItem(item.getId(), currentUser);
                        tableModel.fireTableDataChanged();
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
                        library.returnItem(item.getId(), currentUser);
                        tableModel.fireTableDataChanged();
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

        } else if (role.equals("librarian")) {
            JTextField idField = new JTextField(8);
            JTextField titleField = new JTextField(10);
            JTextField authorField = new JTextField(10);

            JButton addBookButton = new JButton("Dodaj książkę");
            JButton removeButton = new JButton("Usuń zaznaczoną");

            addBookButton.addActionListener(e -> {
                String id = idField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                if (!id.isEmpty() && !title.isEmpty() && !author.isEmpty()) {
                    Book book = new Book(id, title, author);
                    library.addItem(book);
                    tableModel.addItem(book);
                    idField.setText("");
                    titleField.setText("");
                    authorField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola.");
                }
            });

            removeButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Item item = tableModel.getItemAt(row);
                    library.getAllItems().remove(item);
                    tableModel.removeItemAt(row);
                } else {
                    JOptionPane.showMessageDialog(this, "Proszę wybrać przedmiot do usunięcia.");
                }
            });

            for (Item item : library.getAllItems()) {
                tableModel.addItem(item);
            }

            JPanel formPanel = new JPanel();
            formPanel.add(new JLabel("ID:"));
            formPanel.add(idField);
            formPanel.add(new JLabel("Tytuł:"));
            formPanel.add(titleField);
            formPanel.add(new JLabel("Autor:"));
            formPanel.add(authorField);
            formPanel.add(addBookButton);
            formPanel.add(removeButton);

            add(scrollPane, BorderLayout.CENTER);
            add(formPanel, BorderLayout.SOUTH);
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static class ItemTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Tytuł", "Autor", "Status"};
        private final List<Item> items = new ArrayList<>();
        private final List<Item> allItems = new ArrayList<>();

        public ItemTableModel() {
            // konstruktor bez argumentów
        }

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
                for (Item item : allItems) {
                    String content = item.getId().toLowerCase() + " " +
                            item.getTitle().toLowerCase() + " " +
                            (item instanceof Book ? ((Book) item).getAuthor().toLowerCase() : "");
                    if (content.contains(text)) {
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
                case 0: return item.getId();
                case 1: return item.getTitle();
                case 2: return (item instanceof Book) ? ((Book) item).getAuthor() : "-";
                case 3: return item.isBorrowed() ? "wypożyczona" : "dostępna";
                default: return "";
            }
        }
    }
}
