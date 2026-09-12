package week03.activity06;

/**
 * Thrown when an invalid amount is provided (negative or zero).
 */
public class InvalidAmountException extends AccountException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
