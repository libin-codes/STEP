package week02.activity05;

/**
 * Thrown when PIN is incorrect or not set.
 */
public class InvalidPinException extends AccountException {

    public InvalidPinException(String message) {
        super(message);
    }
}