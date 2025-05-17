/**
 * InvalidItemException is thrown for invalid item operations (not found, not loanable, etc.).
 */
public class InvalidItemException extends Exception {
    public InvalidItemException(String message) {
        super(message);
    }
}
