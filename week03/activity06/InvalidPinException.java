package week03.activity06;

/**
 * Thrown when PIN is incorrect or not set.
 */
public class InvalidPinException extends AccountException {

    public InvalidPinException(String message) {
        super(message);
    }
}
