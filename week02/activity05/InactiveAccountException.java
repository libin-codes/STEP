package week02.activity05;

/**
 * Thrown when operation is attempted on an inactive account.
 */
public class InactiveAccountException extends AccountException {

    public InactiveAccountException(String message) {
        super(message);
    }
}