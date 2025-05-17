import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> roleBox;

    public LoginScreen() {
        super("Logowanie");
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Imię:"));
        firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Nazwisko:"));
        lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Rola:"));
        roleBox = new JComboBox<>(new String[]{"user", "librarian"});
        add(roleBox);

        JButton loginButton = new JButton("Zaloguj");
        loginButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String role = (String) roleBox.getSelectedItem();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Proszę wprowadzić imię i nazwisko.");
                return;
            }

            dispose();
            new LibraryGUI(role, firstName, lastName);
        });

        add(loginButton);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
