/**
 * Main class to launch the Library Management System.
 */
public class Main {
    public static void main(String[] args) {
        // Create library and load data from files
        Library library = new Library();
        try {
            LibraryFileManager.loadLibrary(library);
        } catch (Exception e) {
            System.err.println("Wystąpił problem podczas wczytywania danych: " + e.getMessage());
        }
        // Launch the login screen (start GUI)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen(library);
            }
        });
    }
}
