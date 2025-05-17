/**
 * OverdueException is thrown when a loaned item has exceeded its due date.
 */
public class OverdueException extends Exception {
    public OverdueException(String message) {
        super(message);
    }
}
