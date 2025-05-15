import javax.swing.*;
import java.awt.*;

/**
 * Ekran logowania – wybór trybu użytkownika.
 */
public class LoginScreen extends JFrame {

    public LoginScreen() {
        super("Logowanie do biblioteki");

        JButton userButton = new JButton("Zaloguj się jako Użytkownik");
        JButton librarianButton = new JButton("Zaloguj się jako Pracownik");

        userButton.addActionListener(e -> {
            dispose(); // zamknij okno logowania
            new LibraryGUI("user"); // otwórz GUI w trybie użytkownika
        });

        librarianButton.addActionListener(e -> {
            dispose();
            new LibraryGUI("librarian"); // otwórz GUI w trybie pracownika
        });

        setLayout(new GridLayout(2, 1, 10, 10));
        add(userButton);
        add(librarianButton);

        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
