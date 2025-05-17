import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

/**
 * LibraryGUI is the main GUI window for the library system, providing different views for librarian and user.
 */
public class LibraryGUI extends JFrame {
    private Library library;
    private Human loggedIn;
    private JList<Item> itemList;
    private DefaultListModel<Item> itemListModel;
    private JList<LoanRecord> historyList;
    private DefaultListModel<LoanRecord> historyListModel;
    // Librarian controls
    private JTextField titleField, yearField, authorField, genreField, issueField;
    private JComboBox<String> typeCombo;
    private JButton addButton, removeButton;
    // User controls
    private JButton borrowButton, returnButton;
    // Logout button
    private JButton logoutButton;

    public LibraryGUI(Library library, Human loggedIn) {
        super("Library System");
        this.library = library;
        this.loggedIn = loggedIn;
        // Common components
        itemListModel = new DefaultListModel<>();
        historyListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        historyList = new JList<>(historyListModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // determine mode by type of loggedIn
        boolean isLibrarian = (loggedIn instanceof Librarian);
        // Build interface
        if (isLibrarian) {
            buildLibrarianView();
        } else {
            buildUserView();
        }
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void buildLibrarianView() {
        Librarian librarian = (Librarian) loggedIn;
        setTitle("Bibliotekarz: " + librarian.getName());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Top label (with salary info)
        JLabel topLabel = new JLabel("Zalogowano jako Bibliotekarz - " + librarian.getName() 
                                     + " (pensja: " + librarian.getSalary() + ")", SwingConstants.CENTER);
        topLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        this.add(topLabel, BorderLayout.NORTH);
        // Center: item list with scroll
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Wszystkie pozycje w bibliotece:"), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(itemList);
        centerPanel.add(scroll, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
        // Bottom: controls for remove and add
        removeButton = new JButton("Usuń wybraną pozycję");
        // Panel for remove button
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removePanel.add(removeButton);
        // Panel for add form
        typeCombo = new JComboBox<>(new String[]{"Książka", "Czasopismo"});
        titleField = new JTextField(10);
        yearField = new JTextField(4);
        authorField = new JTextField(10);
        genreField = new JTextField(10);
        issueField = new JTextField(5);
        addButton = new JButton("Dodaj");
        // Labels
        JLabel typeLabel = new JLabel("Typ:");
        JLabel titleLabel = new JLabel("Tytuł:");
        JLabel yearLabel = new JLabel("Rok:");
        JLabel authorLabel = new JLabel("Autor:");
        JLabel genreLabel = new JLabel("Gatunek:");
        JLabel issueLabel = new JLabel("Numer wydania:");
        // Add form panel
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(typeLabel);
        addPanel.add(typeCombo);
        addPanel.add(titleLabel);
        addPanel.add(titleField);
        addPanel.add(yearLabel);
        addPanel.add(yearField);
        addPanel.add(authorLabel);
        addPanel.add(authorField);
        addPanel.add(genreLabel);
        addPanel.add(genreField);
        addPanel.add(issueLabel);
        addPanel.add(issueField);
        addPanel.add(addButton);
        // Initially show fields for Book
        issueLabel.setVisible(false);
        issueField.setVisible(false);
        // Bottom container
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(removePanel);
        bottomPanel.add(addPanel);
        this.add(bottomPanel, BorderLayout.SOUTH);
        // Populate the item list
        refreshItemList();
        // Action listeners
        removeButton.addActionListener(e -> {
            Item selected = itemList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Wybierz pozycję do usunięcia.", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                library.removeItem(selected.getId());
                refreshItemList();
                JOptionPane.showMessageDialog(this, "Usunięto pozycję: \"" + selected.getTitle() + "\"");
            } catch (InvalidItemException ex) {
                JOptionPane.showMessageDialog(this, "Nie można usunąć: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        addButton.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String title = titleField.getText().trim();
            String yearText = yearField.getText().trim();
            if (title.isEmpty() || yearText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tytuł i rok są wymagane.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int year;
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Rok musi być liczbą.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if (type.equals("Książka")) {
                    String author = authorField.getText().trim();
                    String genre = genreField.getText().trim();
                    if (author.isEmpty() || genre.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Autor i gatunek są wymagane dla książki.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    library.addBook(title, year, author, genre);
                } else {
                    String issueText = issueField.getText().trim();
                    if (issueText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Numer wydania jest wymagany.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int issue;
                    try {
                        issue = Integer.parseInt(issueText);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Numer wydania musi być liczbą.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    library.addMagazine(title, year, issue);
                }
                // Clear input fields
                titleField.setText("");
                yearField.setText("");
                authorField.setText("");
                genreField.setText("");
                issueField.setText("");
                // Refresh list
                refreshItemList();
                JOptionPane.showMessageDialog(this, "Dodano nową pozycję: \"" + title + "\"");
            } catch (InvalidItemException ex) {
                JOptionPane.showMessageDialog(this, "Błąd dodawania: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        typeCombo.addActionListener(e -> {
            boolean isBook = typeCombo.getSelectedItem().equals("Książka");
            authorField.setVisible(isBook);
            genreField.setVisible(isBook);
            authorLabel.setVisible(isBook);
            genreLabel.setVisible(isBook);
            issueField.setVisible(!isBook);
            issueLabel.setVisible(!isBook);
            // Repack the frame to adjust layout
            this.pack();
        });
        this.pack();

        logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> {
            this.dispose();
            new LoginScreen(library);
        });
        this.add(logoutButton, BorderLayout.EAST);
        
    }

    private void buildUserView() {
        User user = (User) loggedIn;
        setTitle("Użytkownik: " + user.getName());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Top label
        JLabel topLabel = new JLabel("Zalogowano jako Użytkownik - " + user.getName(), SwingConstants.CENTER);
        topLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        this.add(topLabel, BorderLayout.NORTH);
        
        logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> {
            this.dispose();
            new LoginScreen(library); // lub inna klasa od logowania, jeśli się inaczej nazywa
        });

        // Left panel for all items
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Wszystkie pozycje:"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);
        borrowButton = new JButton("Wypożycz");
        JPanel leftBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftBottom.add(borrowButton);
        leftPanel.add(leftBottom, BorderLayout.SOUTH);
        // Right panel for loan history
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Historia wypożyczeń:"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(historyList), BorderLayout.CENTER);
        returnButton = new JButton("Zwróć");
        JPanel rightBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightBottom.add(returnButton);
        rightPanel.add(rightBottom, BorderLayout.SOUTH);
        // Split pane to show both panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);
        this.add(splitPane, BorderLayout.CENTER);
        // Populate lists
        refreshItemList();
        refreshHistoryList();
        // Action listeners
        borrowButton.addActionListener(e -> {
            Item selected = itemList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Wybierz pozycję do wypożyczenia.", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                library.borrowItem(user, selected.getId());
                refreshItemList();
                refreshHistoryList();
                JOptionPane.showMessageDialog(this, "Wypożyczono: \"" + selected.getTitle() + "\"");
            } catch (OverdueException oe) {
                // If overdue thrown on borrow, it means user has overdue items
                JOptionPane.showMessageDialog(this, oe.getMessage(), "Brak możliwości wypożyczenia", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidItemException ex) {
                JOptionPane.showMessageDialog(this, "Nie można wypożyczyć: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        returnButton.addActionListener(e -> {
            LoanRecord selectedRecord = historyList.getSelectedValue();
            if (selectedRecord == null || selectedRecord.isReturned()) {
                JOptionPane.showMessageDialog(this, "Wybierz pozycję do zwrotu (spośród aktualnie wypożyczonych).", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Item itemToReturn = selectedRecord.getItem();
            try {
                library.returnItem(user, itemToReturn.getId());
                refreshItemList();
                refreshHistoryList();
                JOptionPane.showMessageDialog(this, "Zwrócono: \"" + itemToReturn.getTitle() + "\"");
            } catch (OverdueException oe) {
                // Item returned but was overdue
                refreshItemList();
                refreshHistoryList();
                JOptionPane.showMessageDialog(this, "Przedmiot zwrócono po terminie! Możliwe naliczenie kary.", "Przeterminowany zwrot", JOptionPane.WARNING_MESSAGE);
            } catch (InvalidItemException ex) {
                JOptionPane.showMessageDialog(this, "Nie można zwrócić: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Dodaj przycisk Wyloguj na dole
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.setSize(800, 600);
    }

    /**
     * Refreshes the list of items displayed (library inventory).
     */
    private void refreshItemList() {
        itemListModel.clear();
        for (Item item : library.getItems().values()) {
            itemListModel.addElement(item);
        }
    }
    /**
     * Refreshes the history list for the user (loan records).
     */
    private void refreshHistoryList() {
        historyListModel.clear();
        if (loggedIn instanceof User) {
            User user = (User) loggedIn;
            for (LoanRecord rec : user.getLoanHistory()) {
                historyListModel.addElement(rec);
            }
        }
    }
}
