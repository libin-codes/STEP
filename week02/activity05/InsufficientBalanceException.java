package week02.activity05;

/**
 * Thrown when attempting to withdraw more than available balance.
 */
public class InsufficientBalanceException extends AccountException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}