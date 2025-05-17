import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginScreen is a simple GUI for selecting the user role (Librarian or User) at startup.
 */
public class LoginScreen extends JFrame {
    private Library library;

    public LoginScreen(Library library) {
        super("Library System - Wybór roli");
        this.library = library;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 150);
        this.setLayout(new GridLayout(3, 1));

        JLabel prompt = new JLabel("Zaloguj się jako:", SwingConstants.CENTER);
        prompt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        this.add(prompt);

        // Buttons for roles
        JPanel buttonPanel = new JPanel();
        JButton librarianButton = new JButton("Bibliotekarz");
        JButton userButton = new JButton("Użytkownik");
        buttonPanel.add(librarianButton);
        buttonPanel.add(userButton);
        this.add(buttonPanel);

        // Info label at bottom
        JLabel infoLabel = new JLabel("Wybierz rolę, aby rozpocząć", SwingConstants.CENTER);
        this.add(infoLabel);

        // Bibliotekarz
        librarianButton.addActionListener(e -> {
            Librarian lib = new Librarian("Bibliotekarz", 5000.0);
            new LibraryGUI(library, lib);
            dispose();
        });

        // Użytkownik
        userButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Podaj swoje imię:", "Logowanie", JOptionPane.QUESTION_MESSAGE);
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Imię użytkownika nie może być puste.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            username = username.trim();

            // Szukamy użytkownika w zapisanych
            User user = library.getUsers().get(username);
            if (user == null) {
                user = new User(username);
                library.addUser(user);
            }

            new LibraryGUI(library, user);
            dispose();
        });

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
